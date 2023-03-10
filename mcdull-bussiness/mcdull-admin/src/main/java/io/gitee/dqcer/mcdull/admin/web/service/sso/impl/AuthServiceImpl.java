package io.gitee.dqcer.mcdull.admin.web.service.sso.impl;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.admin.framework.auth.ISecurityService;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.UserConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.LoginDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.admin.model.enums.MenuTypeEnum;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.CurrentUserInfVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MenuTreeVo;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MetaVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RouterVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserLoginRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IMenuManager;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IRoleManager;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IUserManager;
import io.gitee.dqcer.mcdull.admin.web.service.common.ICaptchaService;
import io.gitee.dqcer.mcdull.admin.web.service.sso.IAuthService;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.enums.AuthCodeEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUser;
import io.gitee.dqcer.mcdull.framework.base.storage.SsoConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.util.TreeUtil;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ?????????????????? ???????????????
 *
 * @author dqcer
 * @since 2023/01/11 22:01:06
 */
@Service
public class AuthServiceImpl implements IAuthService, ISecurityService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IUserLoginRepository userLoginRepository;

    @Resource
    private RedissonCache redisClient;

    @Resource
    private CacheChannel cacheChannel;

    @Resource
    private IUserManager userManager;

    @Resource
    private IRoleManager roleManager;

    @Resource
    private IMenuManager menuManager;

    @Resource
    private ICaptchaService captchaService;

    /**
     * ??????
     *
     * @param loginDTO ??????dto
     * @return {@link Result}<{@link String}>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> login(LoginDTO loginDTO) {

        boolean validateResult = captchaService.validateCaptcha(loginDTO.getCode(), loginDTO.getUuid());
        if (!validateResult) {
            return Result.error("???????????????");
        }


        String account = loginDTO.getUsername();
        UserDO userEntity = userRepository.queryUserByAccount(account);
        if (null == userEntity) {
            log.warn("??????????????? account: {}", account);
            return Result.error(AuthCodeEnum.NOT_EXIST);
        }
        String password = userEntity.getPassword();

        if (!password.equals(Sha1Util.getSha1(loginDTO.getPassword() + userEntity.getSalt()))) {
            log.warn("??????????????????");
            return Result.error(AuthCodeEnum.NOT_EXIST);
        }
        if (!userEntity.getStatus().equals(StatusEnum.ENABLE.getCode())) {
            log.warn("??????????????? account: {}", account);
            return Result.error(AuthCodeEnum.DISABLE);
        }
        if (!userEntity.getDelFlag().equals(DelFlayEnum.NORMAL.getCode())) {
            log.warn("?????????????????? account: {}", account);
            return Result.error(AuthCodeEnum.NOT_EXIST);
        }

        Long userId = userEntity.getId();

        //  ??????7?????????
        String token = RandomUtil.uuid();
        CacheUser cacheUser = new CacheUser().setUserId(userId).setLastActiveTime(LocalDateTime.now());
        cacheChannel.put(MessageFormat.format(SsoConstant.SSO_TOKEN, token), cacheUser, SsoConstant.SSO_TOKEN_NAMESPACE_TIMEOUT);

        //  ??????????????????
        userRepository.updateLoginTimeById(userId);

        //  ??????????????????
        userLoginRepository.saveLoginInfoByUserIdAndToken(userId, token);

        return Result.ok(token);
    }

    /**
     * token??????
     *
     * @param token ??????
     * @return {@link Result}<{@link Long}>
     */
    @Override
    public Result<Long> tokenValid(String token) {
        String tokenKey = MessageFormat.format(SsoConstant.SSO_TOKEN, token);
        CacheUser user = cacheChannel.get(tokenKey, CacheUser.class);

        if (log.isDebugEnabled()) {
            log.debug("CacheUser:{}", user);
        }

        if (ObjUtil.isNull(user)) {
            log.warn("token valid:  7?????????????????????");
            return Result.error(CodeEnum.UN_AUTHORIZATION);
        }

        if (CacheUser.OFFLINE.equals(user.getOnlineStatus())) {
            log.warn("token valid:  ????????????");
            return Result.error(CodeEnum.OTHER_LOGIN);
        }

        if (CacheUser.LOGOUT.equals(user.getOnlineStatus())) {
            log.warn("token valid:  ?????????????????????");
            return Result.error(CodeEnum.LOGOUT);
        }

        LocalDateTime lastActiveTime = user.getLastActiveTime();

        LocalDateTime now = LocalDateTime.now();
        // TODO: 2022/12/25 ??????????????? ????????????????????????
        int expirationTime = 30;
        if (lastActiveTime.plusMinutes(expirationTime).isBefore(now)) {
            log.warn("token valid:  ?????????????????????");
            return Result.error(CodeEnum.TIMEOUT_LOGIN);
        }

        // ?????????????????? lastActiveTime + 2 < now
        int updateFrequency = 2;
        if (lastActiveTime.plusMinutes(updateFrequency).isBefore(now)) {
            redisClient.putIfExists(tokenKey, user.setLastActiveTime(now));
        }

        return Result.ok(user.getUserId());
    }

    /**
     * ??????????????????
     *
     * @param userId ??????id
     * @return {@link Result}<{@link List}<{@link UserPowerVO}>>
     */
    @Override
    public List<UserPowerVO> queryResourceModules(Long userId) {
        return userRepository.queryResourceModules(userId);
    }

    /**
     * ??????
     *
     * @return {@link Result<String>}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> logout() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException();
        }
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader(HttpHeaderConstants.TOKEN);
        String tokenKey = MessageFormat.format(SsoConstant.SSO_TOKEN, token);
        CacheUser user = redisClient.get(tokenKey, CacheUser.class);
        if (user == null) {
            log.error("redis ?????? token?????????????????????????????????");
            return Result.ok();
        }
        user.setOnlineStatus(CacheUser.LOGOUT);
        redisClient.putIfExists(tokenKey, user);

        //  ??????????????????
        userLoginRepository.saveLogoutInfoByUserIdAndToken(user.getUserId(), token);
        return Result.ok();
    }

    /**
     * ????????????????????????
     *
     * @return {@link Result}<{@link CurrentUserInfVO}>
     */
    @Override
    public Result<CurrentUserInfVO> getCurrentUserInfo() {
        CurrentUserInfVO vo = new CurrentUserInfVO();
        Long userId = UserContextHolder.getSession().getUserId();
        UserDO user = userRepository.getById(userId);
        UserVO userVO = UserConvert.entity2VO(user);
        vo.setUser(userVO);

        List<RoleDO> userRoles = userManager.getUserRoles(userId);
        Set<String> roles = userRoles.stream().map(RoleDO::getCode).collect(Collectors.toSet());
        vo.setRoles(roles);

        Set<String> permissions = new HashSet<>();
        permissions.add("*:*:*");

        vo.setPermissions(permissions);

        return Result.ok(vo);
    }

    /**
     * ??????????????????????????????
     *
     * @return {@link Result}<{@link List}<{@link RouterVO}>>
     */
    @Override
    public Result<List<RouterVO>> getCurrentUserRouters() {
        List<RouterVO> voList = new ArrayList<>();
        Long userId = UserContextHolder.getSession().getUserId();
        List<RoleDO> userRoles = userManager.getUserRoles(userId);
        if (userRoles.isEmpty()) {
            return Result.ok(voList);
        }
        RoleDO roleDO = userRoles.get(1);
        Long roleId = roleDO.getId();

        List<MenuDO> menus = roleManager.getMenuByRole(roleId);

        List<MenuTreeVo> treeVoList = new ArrayList<>();
        for (MenuDO menu : menus) {
            treeVoList.add(convert(menu));
        }

        // tree vo
        List<MenuTreeVo> treeObjects = TreeUtil.getChildTreeObjects(treeVoList, 0L);

        return Result.ok(this.buildMenus(treeObjects));
    }

    /**
     * ????????????
     *
     * @param treeObjects ?????????
     * @return {@link List}<{@link RouterVO}>
     */
    private List<RouterVO> buildMenus(List<MenuTreeVo> treeObjects) {
        List<RouterVO> routers = new LinkedList<>();

        for (MenuTreeVo treeVo : treeObjects) {

            MenuDO menu = convertDO(treeVo);
            RouterVO router = new RouterVO();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(StrUtil.upperFirst(menu.getPath()));
            router.setPath(menuManager.getRouterPath(menu));
            router.setComponent(menuManager.getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVO()
                    .setTitle(menu.getName())
                    .setIcon(menu.getIcon())
                    .setNoCache("1".equals(menu.getIsCache()))
                    .setLink(null));
            List<MenuTreeVo> cMenus = treeVo.getChildren();
            if (!cMenus.isEmpty() && MenuTypeEnum.DIRECTORY.getCode().equals(treeVo.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(this.buildMenus(cMenus));
            } else if (menuManager.isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVO> childrenList = new ArrayList<>();
                RouterVO children = new RouterVO();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(menu.getPath());
                children.setMeta(new MetaVO()
                        .setTitle(menu.getName())
                        .setIcon(menu.getIcon())
                        .setNoCache("1".equals(menu.getIsCache()))
                        .setLink(null));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && menuManager.isInnerLink(menu)) {
                router.setMeta(new MetaVO().setTitle(menu.getName()).setIcon(menu.getIcon()));
                router.setPath("/");
                List<RouterVO> childrenList = new ArrayList<>();
                RouterVO children = new RouterVO();
                String routerPath = menuManager.innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent("InnerLink");
                children.setName(routerPath);
                children.setMeta(new MetaVO().setTitle(menu.getName()).setIcon(menu.getIcon()).setLink(menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    private MenuDO convertDO(MenuTreeVo treeVo) {
        MenuDO menuDO = new MenuDO();
        menuDO.setName(treeVo.getName());
        menuDO.setParentId(treeVo.getParentId());
        menuDO.setOrderNum(treeVo.getOrderNum());
        menuDO.setPath(treeVo.getPath());
        menuDO.setComponent(treeVo.getComponent());
        menuDO.setQuery(treeVo.getQuery());
        menuDO.setIsFrame(treeVo.getIsFrame());
        menuDO.setIsCache(treeVo.getIsCache());
        menuDO.setMenuType(treeVo.getMenuType());
        menuDO.setVisible(treeVo.getVisible());
        menuDO.setStatus(treeVo.getStatus());
        menuDO.setPerms(treeVo.getPerms());
        menuDO.setIcon(treeVo.getIcon());
        menuDO.setCreatedBy(treeVo.getCreatedBy());
        menuDO.setUpdatedTime(treeVo.getUpdatedTime());
        menuDO.setUpdatedBy(treeVo.getUpdatedBy());
        menuDO.setRemark(treeVo.getRemark());
        menuDO.setId(treeVo.getId());
        return menuDO;
    }

    private MenuTreeVo convert(MenuDO menu) {
        MenuTreeVo menuTreeVo = new MenuTreeVo();
        menuTreeVo.setName(menu.getName());
        menuTreeVo.setOrderNum(menu.getOrderNum());
        menuTreeVo.setPath(menu.getPath());
        menuTreeVo.setComponent(menu.getComponent());
        menuTreeVo.setQuery(menu.getQuery());
        menuTreeVo.setIsFrame(menu.getIsFrame());
        menuTreeVo.setIsCache(menu.getIsCache());
        menuTreeVo.setMenuType(menu.getMenuType());
        menuTreeVo.setVisible(menu.getVisible());
        menuTreeVo.setStatus(menu.getStatus());
        menuTreeVo.setPerms(menu.getPerms());
        menuTreeVo.setIcon(menu.getIcon());
        menuTreeVo.setCreatedBy(menu.getCreatedBy());
        menuTreeVo.setUpdatedTime(menu.getUpdatedTime());
        menuTreeVo.setUpdatedBy(menu.getUpdatedBy());
        menuTreeVo.setRemark(menu.getRemark());
        menuTreeVo.setId(menu.getId());
        menuTreeVo.setParentId(menu.getParentId());
        return menuTreeVo;
    }



}

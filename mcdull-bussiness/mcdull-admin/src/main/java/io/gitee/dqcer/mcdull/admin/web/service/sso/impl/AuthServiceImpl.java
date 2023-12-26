package io.gitee.dqcer.mcdull.admin.web.service.sso.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import io.gitee.dqcer.mcdull.admin.framework.auth.ISecurityService;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.MenuConvert;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.UserConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.LoginDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserLoginDO;
import io.gitee.dqcer.mcdull.admin.model.enums.LoginOperationTypeEnum;
import io.gitee.dqcer.mcdull.admin.model.enums.MenuTypeEnum;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.*;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IMenuRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserLoginRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IMenuManager;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IRoleManager;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IUserManager;
import io.gitee.dqcer.mcdull.admin.web.service.common.ICaptchaService;
import io.gitee.dqcer.mcdull.admin.web.service.sso.IAuthService;
import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.enums.AuthCodeEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.CacheUser;
import io.gitee.dqcer.mcdull.framework.base.storage.SsoConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.util.TreeUtil;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserSession;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 身份验证服务 业务实现类
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
    private IMenuRepository menuRepository;

    @Resource
    private ICaptchaService captchaService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> login(LoginDTO loginDTO) {

        HttpServletRequest request = ServletUtil.getRequest();

        UserLoginDO userLoginDO = this.builderLoginOrLogoutInfo(loginDTO.getUsername(),
                request.getHeader(HttpHeaders.USER_AGENT), LoginOperationTypeEnum.LOGIN);

//        boolean validateResult = captchaService.validateCaptcha(loginDTO.getCode(), loginDTO.getUuid());
        boolean validateResult = true;
        if (!validateResult) {
            String error = "验证码错误";
            this.listener(userLoginDO, UserLoginDO.FAIL, error);
            return Result.error(error);
        }
        String account = loginDTO.getUsername();
        UserDO userEntity = userRepository.queryUserByAccount(account);
        if (null == userEntity) {
            log.warn("账号不存在 account: {}", account);
            this.listener(userLoginDO, UserLoginDO.FAIL, AuthCodeEnum.NOT_EXIST.getMessage());
            return Result.error(AuthCodeEnum.NOT_EXIST);
        }
        String password = userEntity.getPassword();

        if (!password.equals(Sha1Util.getSha1(loginDTO.getPassword() + userEntity.getSalt()))) {
            log.warn("用户密码错误");
            this.listener(userLoginDO, UserLoginDO.FAIL, AuthCodeEnum.NOT_EXIST.getMessage());
            return Result.error(AuthCodeEnum.NOT_EXIST);
        }
        if (!userEntity.getStatus().equals(StatusEnum.ENABLE.getCode())) {
            log.warn("账号已停用 account: {}", account);
            this.listener(userLoginDO, UserLoginDO.FAIL, AuthCodeEnum.DISABLE.getMessage());
            return Result.error(AuthCodeEnum.DISABLE);
        }
//        if (!userEntity.getDelFlag().equals(DelFlayEnum.NORMAL.getCode())) {
//            log.warn("账号已被删除 account: {}", account);
//            this.listener(userLoginDO, UserLoginDO.FAIL, AuthCodeEnum.NOT_EXIST.getMessage());
//            return Result.error(AuthCodeEnum.NOT_EXIST);
//        }

        Long userId = userEntity.getId();

        //  强制7天过期
        String token = RandomUtil.uuid();
        CacheUser cacheUser = new CacheUser().setUserId(userId).setLastActiveTime(LocalDateTime.now()).setUserType(userEntity.getType());
        cacheChannel.put(MessageFormat.format(SsoConstant.SSO_TOKEN, token), cacheUser, SsoConstant.SSO_TOKEN_NAMESPACE_TIMEOUT);

        //  更新登录时间
        userRepository.updateLoginTimeById(userId);

        this.listener(userLoginDO, UserLoginDO.OK, CodeEnum.SUCCESS.getMessage());

        return Result.success(token);
    }

    private void listener(UserLoginDO userLoginDO, String type, String error) {
        userLoginDO.setType(type);
        userLoginDO.setRemark(error);
        userLoginRepository.save(userLoginDO);
    }

    private UserLoginDO builderLoginOrLogoutInfo(String account, String agentUrl, LoginOperationTypeEnum operationType) {
        UserAgent agent = UserAgentUtil.parse(agentUrl);
        UserLoginDO userLoginDO = new UserLoginDO();
        userLoginDO.setAccount(account);
        String browserName = agent.getBrowser().getName();
        if (Browser.Unknown.getName().equals(browserName)) {
            browserName = agentUrl;
        }
        userLoginDO.setBrowser(browserName);
        userLoginDO.setOs(agent.getOs().getName());
        userLoginDO.setOperationType(operationType.getCode());
        return userLoginDO;
    }

    /**
     * token验证
     *
     * @param token 令牌
     * @return {@link Result}<{@link Long}>
     */
    @Override
    public Result<UserSession> tokenValid(String token) {
        String tokenKey = MessageFormat.format(SsoConstant.SSO_TOKEN, token);
        CacheUser user = cacheChannel.get(tokenKey, CacheUser.class);

        if (log.isDebugEnabled()) {
            log.debug("CacheUser:{}", user);
        }

        if (ObjUtil.isNull(user)) {
            log.warn("token valid:  7天强制过期下线");
            return Result.error(CodeEnum.UN_AUTHORIZATION);
        }

        if (CacheUser.OFFLINE.equals(user.getOnlineStatus())) {
            log.warn("token valid:  异地登录");
            return Result.error(CodeEnum.OTHER_LOGIN);
        }

        if (CacheUser.LOGOUT.equals(user.getOnlineStatus())) {
            log.warn("token valid:  客户端主动退出");
            return Result.error(CodeEnum.LOGOUT);
        }

        LocalDateTime lastActiveTime = user.getLastActiveTime();

        LocalDateTime now = LocalDateTime.now();
        // TODO: 2022/12/25 过期时间， 需要写入配置文件
        int expirationTime = 30;
        if (lastActiveTime.plusMinutes(expirationTime).isBefore(now)) {
            log.warn("token valid:  用户操作已过期");
            return Result.error(CodeEnum.TIMEOUT_LOGIN);
        }

        // 控制更新频率 lastActiveTime + 2 < now
        int updateFrequency = 2;
        if (lastActiveTime.plusMinutes(updateFrequency).isBefore(now)) {
            cacheChannel.put(tokenKey, user.setLastActiveTime(now), SsoConstant.SSO_TOKEN_NAMESPACE_TIMEOUT);
        }
        UserSession session = new UserSession();
        session.setUserId(user.getUserId());
        session.setType(user.getUserType());
        return Result.success(session);
    }

    /**
     * 查询资源模块
     *
     * @param userId 用户id
     * @return {@link Result}<{@link List}<{@link UserPowerVO}>>
     */
    @Override
    public List<UserPowerVO> queryResourceModules(Long userId) {
        return userRepository.queryResourceModules(userId);
    }

    /**
     * 注销
     *
     * @return {@link Result<String>}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> logout() {
        HttpServletRequest request = ServletUtil.getRequest();
        String token = request.getHeader(HttpHeaderConstants.TOKEN);
        String tokenKey = MessageFormat.format(SsoConstant.SSO_TOKEN, token);
        CacheUser user = cacheChannel.get(tokenKey, CacheUser.class);
        if (user == null) {
            log.error("redis 缓存 token过期，这里需要优化处理");
            return Result.success();
        }
        user.setOnlineStatus(CacheUser.LOGOUT);
        cacheChannel.put(tokenKey, user, SsoConstant.SSO_TOKEN_NAMESPACE_TIMEOUT);

        //  记录注销信息
        Long userId = UserContextHolder.currentUserId();
        UserDO userDO = userRepository.getById(userId);
        UserLoginDO userLoginDO = this.builderLoginOrLogoutInfo(userDO.getAccount(), request.getHeader(HttpHeaders.USER_AGENT), LoginOperationTypeEnum.LOGOUT);
        this.listener(userLoginDO, UserLoginDO.OK, CodeEnum.SUCCESS.getMessage());
        return Result.success();
    }

    /**
     * 得到当前用户信息
     *
     * @return {@link Result}<{@link CurrentUserInfVO}>
     */
    @Override
    public Result<CurrentUserInfVO> getCurrentUserInfo() {
        CurrentUserInfVO vo = new CurrentUserInfVO();
        Long userId = UserContextHolder.currentUserId();
        UserDO user = userRepository.getById(userId);
        UserVO userVO = UserConvert.entityToVO(user);
        vo.setUser(userVO);

        List<RoleDO> userRoles = userManager.getUserRoles(userId);
        Set<String> roles = userRoles.stream().map(RoleDO::getCode).collect(Collectors.toSet());
        vo.setRoles(roles);

        Set<String> permissions = new HashSet<>();
        permissions.add("*:*:*");

        vo.setPermissions(permissions);

        return Result.success(vo);
    }

    /**
     * 获得当前用户路由信息
     *
     * @return {@link Result}<{@link List}<{@link RouterVO}>>
     */
    @Override
    public Result<List<RouterVO>> getCurrentUserRouters() {
        List<RouterVO> voList = new ArrayList<>();
        Long userId = UserContextHolder.currentUserId();
        List<RoleDO> userRoles = userManager.getUserRoles(userId);
        if (userRoles.isEmpty()) {
            return Result.success(voList);
        }
        RoleDO roleDO = userRoles.get(1);
        Long roleId = roleDO.getId();

        List<Long> roles = new ArrayList<>();
        roles.add(roleId);
        List<MenuDO> menuList = new ArrayList<>();
        if (UserContextHolder.isAdmin()) {
            menuList = menuManager.getAllMenu();
        } else {
            menuList = roleManager.getMenuByRole(roles);
        }


        List<MenuTreeVo> treeVoList = new ArrayList<>();
        for (MenuDO menu : menuList) {
            treeVoList.add(MenuConvert.convertMenuTreeVo(menu));
        }

        // tree vo
        List<MenuTreeVo> treeObjects = TreeUtil.getChildTreeObjects(treeVoList, 0L);

        return Result.success(this.buildMenus(treeObjects));
    }

    /**
     * 构建菜单
     *
     * @param treeObjects 树对象
     * @return {@link List}<{@link RouterVO}>
     */
    private List<RouterVO> buildMenus(List<MenuTreeVo> treeObjects) {
        List<RouterVO> routers = new LinkedList<>();

        for (MenuTreeVo treeVo : treeObjects) {

            MenuDO menu = MenuConvert.convertDO(treeVo);
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
            if (CollUtil.isNotEmpty(cMenus)  && MenuTypeEnum.DIRECTORY.getCode().equals(treeVo.getMenuType())) {
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



}

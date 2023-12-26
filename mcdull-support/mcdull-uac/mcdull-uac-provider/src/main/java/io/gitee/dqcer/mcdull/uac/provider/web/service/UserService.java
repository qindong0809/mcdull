package io.gitee.dqcer.mcdull.uac.provider.web.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.Md5Util;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.mdc.client.constants.DictEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.UserConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRoleRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.mdc.IDictManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.uac.IUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 用户服务
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IUserManager userManager;

    @Resource
    private IUserRoleRepository userRoleRepository;

    @Resource
    private IDictManager dictManager;

    
    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    public Result<PagedVO<UserVO>> listByPage(UserLiteDTO dto) {
        Page<UserDO> entityPage = userRepository.selectPage(dto);
        List<UserVO> voList = new ArrayList<>();
        List<UserDO> records = entityPage.getRecords();
        Map<String, String> dictMap = new HashMap<>(records.size());
        if (CollUtil.isNotEmpty(records)) {
            dictMap = dictManager.codeNameMap(DictEnum.STATUS_TYPE);
        }
        for (UserDO entity : records) {
            UserVO vo = userManager.entity2VO(entity);
            vo.setStatusStr(dictMap.getOrDefault(vo.getStatus(), StrUtil.EMPTY));
            voList.add(vo);
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }

    /**
     * 详情
     *
     * @param dto dto
     * @return {@link Result}<{@link UserVO}>
     */
    public Result<UserVO> detail(UserLiteDTO dto) {
        return Result.success(userManager.entity2VO(userRepository.getById(dto.getId())));
    }

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回新增主键}
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> insert(UserLiteDTO dto) {
        LambdaQueryWrapper<UserDO> query = Wrappers.lambdaQuery();
        query.eq(UserDO::getAccount, dto.getAccount());
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<UserDO> list = userRepository.list(query);
        if (!list.isEmpty()) {
            return Result.error(CodeEnum.DATA_EXIST);
        }

        UserDO entity = UserConvert.dto2Entity(dto);

        String salt = RandomUtil.uuid();
        String password = Sha1Util.getSha1(Md5Util.getMd5(dto.getAccount() + salt));
        entity.setSalt(salt);
        entity.setPassword(password);
        entity.setType(1);
        Long userId = userRepository.insert(entity);

        userRoleRepository.updateByUserId(userId, dto.getRoleIds());

        return Result.success(userId);
    }

    /**
     * 更新状态
     *
     * @param dto dto
     * @return {@link Result}<{@link Long}>
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> updateStatus(UserLiteDTO dto) {
        Long id = dto.getId();
        UserDO dbData = userRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        String status = dto.getStatus();
        if (dbData.getStatus().equals(status)) {
            log.warn("数据已存在 id: {} status: {}", id, status);
            return Result.error(CodeEnum.DATA_EXIST);
        }

        UserDO entity = new UserDO();
        entity.setId(id);
        entity.setStatus(status);

        boolean success = userRepository.updateById(entity);
        if (!success) {
            log.error("数据更新失败，entity:{}", entity);
            throw new BusinessException(CodeEnum.DB_ERROR);
        }

        return Result.success(id);
    }

    /**
     * 删除
     *
     * @param dto dto
     * @return {@link Result}<{@link Long}>
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> delete(UserLiteDTO dto) {
        Long id = dto.getId();


        UserDO dbData = userRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        UserDO entity = new UserDO();
        entity.setId(id);

        boolean success = userRepository.updateById(entity);
        if (!success) {
            log.error("数据删除失败，entity:{}", entity);
            throw new BusinessException(CodeEnum.DB_ERROR);
        }

        return Result.success(id);
    }

    /**
     * 重置密码
     *
     * @param dto dto
     * @return {@link Result}<{@link Long}>
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> resetPassword(UserLiteDTO dto) {
        Long id = dto.getId();
        UserDO entity = userRepository.getById(id);
        if (entity == null) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        String password = Sha1Util.getSha1(Md5Util.getMd5(entity.getAccount() + entity.getSalt()));
        UserDO user = new UserDO();
        user.setId(id);
        user.setPassword(password);
        boolean success = userRepository.updateById(user);
        if (!success) {
            log.error("重置密码失败，entity:{}", user);
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return Result.success(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result<Long> update(UserLiteDTO dto) {
        Long id = dto.getId();
        UserDO entity = userRepository.getById(id);
        if (entity == null) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        UserDO userDO = userRepository.oneByAccount(dto.getAccount());
        if (userDO != null) {
            if (!userDO.getId().equals(id)) {
                log.warn("账号名称已存在 account: {}", dto.getAccount());
                return Result.error(CodeEnum.DATA_EXIST);
            }
        }

        UserDO updateDO = UserConvert.dto2Entity(dto);
        updateDO.setId(id);
        userRepository.updateById(updateDO);

        userRoleRepository.updateByUserId(id, dto.getRoleIds());

        return Result.success(updateDO.getId());
    }

    /**
     * 查询资源模块
     *
     * @param userId 用户id
     * @return {@link Result}<{@link List}<{@link UserPowerVO}>>
     */
    public Result<List<UserPowerVO>> queryResourceModules(Long userId) {
        return Result.success(userRepository.queryResourceModules(userId));
    }
}

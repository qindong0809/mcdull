package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.framework.transformer.IUserTransformerService;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.UserConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IUserManager;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IUserService;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.util.Md5Util;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务 impl
 *
 * @author dqcer
 * @since 2023/01/15 13:01:30
 */
@Service
public class UserServiceImpl implements IUserService, IUserTransformerService {

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IUserManager userManager;

    /**
     * 列表页
     *
     * @param dto dto
     * @return {@link Result < PagedVO < UserVO >>}
     */
    @Override
    public Result<PagedVO<UserVO>> listByPage(UserLiteDTO dto) {
        Page<UserDO> entityPage = userRepository.selectPage(dto);
        List<UserVO> voList = new ArrayList<>();
        for (UserDO entity : entityPage.getRecords()) {
            voList.add(userManager.entityToVo(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    /**
     * 单个详情
     *
     * @param userId userId
     * @return {@link Result<UserVO>}
     */
    @Override
    public Result<UserDetailVO> detail(Long userId) {
        UserDO userDO = userRepository.getById(userId);
        UserDetailVO detailVO = userManager.entityToDetailVo(userDO);
        return Result.ok(detailVO);
    }


    /**
     * 插入
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Override
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
        entity.setStatus(StatusEnum.ENABLE.getCode());
        Long userId = userRepository.insert(entity);

//        userRoleRepository.updateByUserId(userId, dto.getRoleIds());

        return Result.ok(userId);
    }

    /**
     * 更新
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Override
    public Result<Long> update(UserLiteDTO dto) {
        return null;
    }

    /**
     * 更新状态
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Override
    public Result<Long> updateStatus(StatusDTO dto) {
        return null;
    }

    /**
     * 删除
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Override
    public Result<Long> delete(UserLiteDTO dto) {
        return null;
    }

    /**
     * 重置密码
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Override
    public Result<Long> resetPassword(UserLiteDTO dto) {
        return null;
    }

    /**
     * 翻译
     *
     * @param code  编码
     * @return {@link KeyValueVO}
     */
    @Override
    public KeyValueVO<String, String> transformer(String code) {
        UserDO userDO = userRepository.getById(code);
        if (ObjUtil.isNotNull(userDO)) {
            return new KeyValueVO<String, String>().setId(userDO.getId().toString()).setName(userDO.getNickName());
        }
        return new KeyValueVO<>();
    }
}

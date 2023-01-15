package com.dqcer.mcdull.admin.web.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.dto.StatusDTO;
import com.dqcer.framework.base.enums.StatusEnum;
import com.dqcer.framework.base.util.Md5Util;
import com.dqcer.framework.base.util.PageUtil;
import com.dqcer.framework.base.util.RandomUtil;
import com.dqcer.framework.base.util.Sha1Util;
import com.dqcer.framework.base.vo.KeyValueVO;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.CodeEnum;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.admin.framework.transformer.IUserTransformerService;
import com.dqcer.mcdull.admin.model.convert.sys.UserConvert;
import com.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import com.dqcer.mcdull.admin.model.entity.sys.UserDO;
import com.dqcer.mcdull.admin.model.vo.sys.UserVO;
import com.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import com.dqcer.mcdull.admin.web.manager.sys.IUserManager;
import com.dqcer.mcdull.admin.web.service.sys.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务 impl
 *
 * @author dqcer
 * @date 2023/01/15 13:01:30
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
     * @param dto dto
     * @return {@link Result<UserVO>}
     */
    @Override
    public Result<UserVO> detail(UserLiteDTO dto) {
        return null;
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
        return new KeyValueVO<String, String>().setId(userDO.getId().toString()).setName(userDO.getNickname());
    }
}

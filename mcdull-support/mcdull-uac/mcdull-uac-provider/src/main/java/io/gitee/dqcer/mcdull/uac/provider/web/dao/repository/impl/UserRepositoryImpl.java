package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.UserMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户 数据库操作实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link UserEntity}>
     */

    @Override
    public Page<UserEntity> selectPage(UserLiteDTO dto) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(UserEntity::getUsername, keyword)
                    .or().like(UserEntity::getPhone, keyword)
                    .or().like(UserEntity::getEmail, keyword));
        }
        query.orderByDesc(BaseEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), query);
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    @Override
    public Integer insert(UserEntity entity) {
        int row = baseMapper.insert(entity);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    /**
     * 单个根据账户名称
     *
     * @param account 账户
     * @return {@link UserEntity}
     */
    @Override
    public UserEntity get(String account) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        query.eq(UserEntity::getUsername, account);
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<UserEntity> list = list(query);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 更新登录时间通过id
     *
     * @param userId 用户id
     */
    @Override
    public void updateLoginTime(Integer userId, Date nowTime) {
        UserEntity entity = new UserEntity();
        entity.setId(userId);
        entity.setLastLoginTime(nowTime);
        entity.setUpdatedBy(userId);
        int rowSize = baseMapper.updateById(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            throw new DatabaseRowException(I18nConstants.DB_OPERATION_FAILED);
        }
    }


    @Override
    public boolean update(Integer id, boolean inactive) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setInactive(inactive);
        return this.update(entity);
    }

    @Override
    public boolean update(Integer id, String password) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setPassword(password);
        return this.update(entity);
    }

    public boolean update(UserEntity entity) {
        return this.updateById(entity);
    }
}

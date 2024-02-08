package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;
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
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserDO> implements IUserRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link UserDO}>
     */

    @Override
    public Page<UserDO> selectPage(UserLiteDTO dto) {
        LambdaQueryWrapper<UserDO> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(UserDO::getUsername, keyword)
                    .or().like(UserDO::getPhone, keyword)
                    .or().like(UserDO::getEmail, keyword));
        }
        query.orderByDesc(BaseDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    @Override
    public Long insert(UserDO entity) {
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
     * @return {@link UserDO}
     */
    @Override
    public UserDO get(String account) {
        LambdaQueryWrapper<UserDO> query = Wrappers.lambdaQuery();
        query.eq(UserDO::getUsername, account);
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<UserDO> list = list(query);
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
    public void updateLoginTime(Long userId, Date nowTime) {
        UserDO entity = new UserDO();
        entity.setId(userId);
        entity.setLastLoginTime(nowTime);
        entity.setUpdatedBy(userId);
        int rowSize = baseMapper.updateById(entity);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            throw new DatabaseRowException(I18nConstants.DB_OPERATION_FAILED);
        }
    }


    @Override
    public boolean update(Long id, boolean inactive) {
        UserDO entity = new UserDO();
        entity.setId(id);
        entity.setInactive(inactive);
        return this.update(entity);
    }

    @Override
    public boolean update(Long id, String password) {
        UserDO entity = new UserDO();
        entity.setId(id);
        entity.setPassword(password);
        return this.update(entity);
    }

    public boolean update(UserDO entity) {
        return this.updateById(entity);
    }
}

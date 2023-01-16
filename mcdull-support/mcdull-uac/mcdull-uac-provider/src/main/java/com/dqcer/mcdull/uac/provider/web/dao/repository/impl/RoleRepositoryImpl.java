package io.gitee.dqcer.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.framework.base.entity.BaseDO;
import io.gitee.dqcer.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.framework.base.exception.BusinessException;
import io.gitee.dqcer.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.framework.base.util.StrUtil;
import io.gitee.dqcer.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.uac.provider.model.dto.RoleLiteDTO;
import io.gitee.dqcer.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.uac.provider.web.dao.mapper.RoleMapper;
import io.gitee.dqcer.uac.provider.web.dao.repository.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 角色 数据库操作封装实现层
 *
 * @author dqcer
 * @version 2022/12/25
 */
@Service
public class RoleRepositoryImpl extends ServiceImpl<RoleMapper, RoleDO> implements IRoleRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    @Override
    public Page<RoleDO> selectPage(RoleLiteDTO dto) {
        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(RoleDO::getName, keyword));
        }
        query.orderByDesc(BaseDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), query);
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    @Override
    public Long insert(RoleDO entity) {
        entity.setDelFlag(DelFlayEnum.NORMAL.getCode());
        entity.setCreatedBy(UserContextHolder.getSession().getUserId());
        entity.setCreatedTime(new Date());
        int row = baseMapper.insert(entity);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }
}

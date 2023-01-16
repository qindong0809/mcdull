package io.gitee.dqcer.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.framework.base.entity.BaseDO;
import io.gitee.dqcer.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.framework.base.exception.BusinessException;
import io.gitee.dqcer.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.framework.base.util.StrUtil;
import io.gitee.dqcer.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.uac.provider.model.dto.MenuLiteDTO;
import io.gitee.dqcer.uac.provider.model.entity.MenuDO;
import io.gitee.dqcer.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.uac.provider.web.dao.mapper.MenuMapper;
import io.gitee.dqcer.uac.provider.web.dao.repository.IMenuRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 菜单 数据库操作封装实现层
 *
 * @author dqcer
 * @date 2022/12/26
 */
@Service
public class MenuRepositoryImpl extends ServiceImpl<MenuMapper, MenuDO> implements IMenuRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    @Override
    public Page<MenuDO> selectPage(MenuLiteDTO dto) {
        LambdaQueryWrapper<MenuDO> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(MenuDO::getName, keyword));
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
    public Long insert(MenuDO entity) {
        entity.setDelFlag(DelFlayEnum.NORMAL.getCode());
        entity.setCreatedBy(UserContextHolder.getSession().getUserId());
        entity.setCreatedTime(new Date());
        int insert = baseMapper.insert(entity);
        if (insert != 1) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }
}

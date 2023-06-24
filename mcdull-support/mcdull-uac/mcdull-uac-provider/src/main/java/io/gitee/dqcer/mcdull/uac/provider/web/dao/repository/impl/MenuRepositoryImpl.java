package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MenuLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuDO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.MenuMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

/**
 * 菜单 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2022/12/26
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
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }
}

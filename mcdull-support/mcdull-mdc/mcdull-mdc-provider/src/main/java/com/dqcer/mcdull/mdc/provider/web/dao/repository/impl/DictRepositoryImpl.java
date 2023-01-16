package io.gitee.dqcer.mdc.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.framework.base.util.StrUtil;
import io.gitee.dqcer.mdc.provider.model.dto.DictLiteDTO;
import io.gitee.dqcer.mdc.provider.model.entity.DictDO;
import io.gitee.dqcer.mdc.provider.web.dao.mapper.DictMapper;
import io.gitee.dqcer.mdc.provider.web.dao.repository.IDictRepository;
import org.springframework.stereotype.Service;

/**
 * 码表 数据库操作封装实现层
 *
 * @author dqcer
 * @version  2022/12/25
 */
@Service
public class DictRepositoryImpl extends ServiceImpl<DictMapper, DictDO> implements IDictRepository {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link DictDO}>
     */
    @Override
    public Page<DictDO> selectPage(DictLiteDTO dto) {
        LambdaQueryWrapper<DictDO> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(DictDO::getCode, keyword).or().like(DictDO::getSelectType, keyword).or().like(DictDO::getName, keyword));
        }
        query.orderByAsc(DictDO::getSelectType, DictDO::getSort);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), query);
    }

}

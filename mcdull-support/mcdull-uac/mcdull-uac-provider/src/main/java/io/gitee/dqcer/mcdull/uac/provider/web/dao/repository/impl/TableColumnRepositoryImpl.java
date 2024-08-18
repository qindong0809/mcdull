package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.TableColumnEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.TableColumnMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ITableColumnRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Table Column RepositoryImpl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class TableColumnRepositoryImpl
        extends ServiceImpl<TableColumnMapper, TableColumnEntity>  implements ITableColumnRepository {

    @Override
    public List<TableColumnEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<TableColumnEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TableColumnEntity::getId, idList);
        List<TableColumnEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public Page<TableColumnEntity> selectPage(ConfigQueryDTO param) {
        LambdaQueryWrapper<TableColumnEntity> lambda = new QueryWrapper<TableColumnEntity>().lambda();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            lambda.like(TableColumnEntity::getColumns, keyword);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    @Override
    public TableColumnEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Integer insert(TableColumnEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public boolean exist(TableColumnEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public List<TableColumnEntity> selectList(Integer userId) {
        LambdaQueryWrapper<TableColumnEntity> query = Wrappers.lambdaQuery();
        query.eq(TableColumnEntity::getUserId, userId);
        return baseMapper.selectList(query);
    }

    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }
}
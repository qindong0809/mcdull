package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TableColumnUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TableColumnEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.TableColumnMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITableColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
* Table Column Service Impl
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class TableColumnServiceImpl
        extends BasicCurdServiceImpl<TableColumnMapper, TableColumnEntity> implements ITableColumnService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTableColumns(TableColumnUpdateDTO dto) {
        Integer userId = UserContextHolder.userId();
        String string = JSONUtil.parseArray(dto.getColumnList()).toString();
        List<TableColumnEntity> list = this.selectList(userId);
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, TableColumnEntity> map = list.stream()
                    .collect(Collectors.toMap(TableColumnEntity::getTableId, Function.identity()));
            TableColumnEntity columnEntity = map.get(dto.getTableId());
            if (ObjUtil.isNotNull(columnEntity)) {
                columnEntity.setColumns(string);
                super.updateById(columnEntity);
                return;
            }
        }

        TableColumnEntity entity = new TableColumnEntity();
        entity.setColumns(string);
        entity.setTableId(dto.getTableId());
        entity.setUserId(userId);
        this.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTableColumn(Integer tableId) {
        Integer userId = UserContextHolder.userId();
        List<TableColumnEntity> list = this.selectList(userId);
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, TableColumnEntity> map = list.stream()
                    .collect(Collectors.toMap(TableColumnEntity::getTableId, Function.identity()));
            TableColumnEntity columnEntity = map.get(tableId);
            if (ObjUtil.isNotNull(columnEntity)) {
                super.removeById(columnEntity.getId());
                return;
            }
        }
        LogicCheckUtil.throwDataNotExistException(tableId);
    }

    @Override
    public String getTableColumns(Integer tableId) {
        Integer userId = UserContextHolder.userId();
        List<TableColumnEntity> list = this.selectList(userId);
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, TableColumnEntity> map = list.stream()
                    .collect(Collectors.toMap(TableColumnEntity::getTableId, Function.identity()));
            TableColumnEntity columnEntity = map.get(tableId);
            if (ObjUtil.isNotNull(columnEntity)) {
                return columnEntity.getColumns();
            }
        }
        return StrUtil.EMPTY;
    }


    public List<TableColumnEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<TableColumnEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TableColumnEntity::getId, idList);
        return baseMapper.selectList(wrapper);
    }

    public Page<TableColumnEntity> selectPage(ConfigQueryDTO param) {
        LambdaQueryWrapper<TableColumnEntity> lambda = new QueryWrapper<TableColumnEntity>().lambda();
        String keyword = param.getKeyword();
        if (ObjectUtil.isNotNull(keyword)) {
            lambda.like(TableColumnEntity::getColumns, keyword);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    public TableColumnEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    public Integer insert(TableColumnEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    public boolean exist(TableColumnEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    public List<TableColumnEntity> selectList(Integer userId) {
        LambdaQueryWrapper<TableColumnEntity> query = Wrappers.lambdaQuery();
        query.eq(TableColumnEntity::getUserId, userId);
        return baseMapper.selectList(query);
    }

    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}

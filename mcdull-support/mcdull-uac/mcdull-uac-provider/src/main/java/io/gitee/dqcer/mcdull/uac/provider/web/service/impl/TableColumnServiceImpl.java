package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.TableColumnUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.TableColumnEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ITableColumnRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ITableColumnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
* 系统配置 业务实现类
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class TableColumnServiceImpl
        extends BasicServiceImpl<ITableColumnRepository> implements ITableColumnService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTableColumns(TableColumnUpdateDTO dto) {
        Integer userId = UserContextHolder.userId();
        String string = JSONUtil.parseArray(dto.getColumnList()).toString();
        List<TableColumnEntity> list = baseRepository.selectList(userId);
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, TableColumnEntity> map = list.stream().collect(Collectors.toMap(TableColumnEntity::getTableId,
                    Function.identity()));
            TableColumnEntity columnEntity = map.get(dto.getTableId());
            if (ObjUtil.isNotNull(columnEntity)) {
                columnEntity.setColumns(string);
                baseRepository.updateById(columnEntity);
                return;
            }
        }

        TableColumnEntity entity = new TableColumnEntity();
        entity.setColumns(string);
        entity.setTableId(dto.getTableId());
        entity.setUserId(userId);
        baseRepository.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTableColumn(Integer tableId) {
        Integer userId = UserContextHolder.userId();
        List<TableColumnEntity> list = baseRepository.selectList(userId);
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, TableColumnEntity> map = list.stream().collect(Collectors.toMap(TableColumnEntity::getTableId,
                    Function.identity()));
            TableColumnEntity columnEntity = map.get(tableId);
            if (ObjUtil.isNotNull(columnEntity)) {
                baseRepository.removeById(columnEntity.getId());
                return;
            }
        }
        this.throwDataNotExistException(tableId);
    }

    @Override
    public String getTableColumns(Integer tableId) {
        Integer userId = UserContextHolder.userId();
        List<TableColumnEntity> list = baseRepository.selectList(userId);
        if (CollUtil.isNotEmpty(list)) {
            Map<Integer, TableColumnEntity> map = list.stream().collect(Collectors.toMap(TableColumnEntity::getTableId,
                    Function.identity()));
            TableColumnEntity columnEntity = map.get(tableId);
            if (ObjUtil.isNotNull(columnEntity)) {
                return columnEntity.getColumns();
            }
        }
        return StrUtil.EMPTY;
    }
}

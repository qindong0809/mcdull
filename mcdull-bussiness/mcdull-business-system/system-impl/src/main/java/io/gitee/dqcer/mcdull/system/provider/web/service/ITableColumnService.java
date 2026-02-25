package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TableColumnUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.TableColumnEntity;

/**
 * Table Column Service
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ITableColumnService extends IRepository<TableColumnEntity> {

    /**
     * update
     *
     * @param dto DTO
     */
    void updateTableColumns(TableColumnUpdateDTO dto);

    /**
     * delete
     *
     * @param tableId tableId
     */
    void deleteTableColumn(Integer tableId);

    /**
     * get
     *
     * @param tableId tableId
     * @return {@link String }
     */
    String getTableColumns(Integer tableId);
}

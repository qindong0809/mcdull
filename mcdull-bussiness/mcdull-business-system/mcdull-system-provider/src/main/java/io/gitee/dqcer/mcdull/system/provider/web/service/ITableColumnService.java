package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.dto.TableColumnUpdateDTO;

/**
 * Table Column Service
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ITableColumnService {

    void updateTableColumns(TableColumnUpdateDTO dto);

    void deleteTableColumn(Integer tableId);

    String getTableColumns(Integer tableId);
}

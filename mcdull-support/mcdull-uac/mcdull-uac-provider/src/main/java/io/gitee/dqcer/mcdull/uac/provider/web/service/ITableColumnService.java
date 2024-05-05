package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.TableColumnUpdateDTO;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface ITableColumnService {

    void updateTableColumns(TableColumnUpdateDTO dto);

    void deleteTableColumn(Integer tableId);

    String getTableColumns(Integer tableId);
}

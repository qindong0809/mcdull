package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorPreviewForm;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TableQueryForm;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TableColumnVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TableConfigVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TableVO;

import java.util.List;

/**
 * Code generator service
 *
 * @author dqcer
 * @since 2024/7/25 9:19
 */

public interface ICodeGeneratorService {


    List<TableColumnVO> getTableColumns(String table);

    PagedVO<TableVO> queryTableList(TableQueryForm dto);

    TableConfigVO getTableConfig(String table);

    void updateConfig(CodeGeneratorConfigForm dto);

    String preview(CodeGeneratorPreviewForm dto);

    byte[] download(String tableName);
}

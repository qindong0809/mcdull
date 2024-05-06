package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeGeneratorPreviewForm;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.TableQueryForm;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableColumnVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableConfigVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableVO;

import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
public interface ICodeGeneratorService {


    List<TableColumnVO> getTableColumns(String table);

    PagedVO<TableVO> queryTableList(TableQueryForm dto);

    TableConfigVO getTableConfig(String table);

    void updateConfig(CodeGeneratorConfigForm dto);

    String preview(CodeGeneratorPreviewForm dto);
}

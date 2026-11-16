package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorPreviewDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TableQueryFormDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.CodeGeneratorConfigEntity;
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

public interface ICodeGeneratorService extends IRepository<CodeGeneratorConfigEntity> {


    List<TableColumnVO> getTableColumns(String table);

    PagedVO<TableVO> queryTableList(TableQueryFormDTO dto);

    TableConfigVO getTableConfig(String table);

    void updateConfig(CodeGeneratorConfigDTO dto);

    String preview(CodeGeneratorPreviewDTO dto);

    byte[] download(String tableName);
}

package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeGeneratorPreviewForm;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.TableQueryForm;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.CodeGeneratorConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableColumnVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableConfigVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ICodeGeneratorConfigRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ICodeGeneratorService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class CodeGeneratorServiceImpl extends BasicServiceImpl<ICodeGeneratorConfigRepository> implements ICodeGeneratorService {


    @Override
    public List<TableColumnVO> getTableColumns(String table) {
       return baseRepository.getByTable(table);
    }

    @Override
    public PagedVO<TableVO> queryTableList(TableQueryForm dto) {
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        List<TableVO> tableVOList = baseRepository.queryTableList(page, dto);
        return PageUtil.toPage(tableVOList, page);
    }

    @Override
    public TableConfigVO getTableConfig(String table) {
        return null;
    }

    @Override
    public void updateConfig(CodeGeneratorConfigForm dto) {

    }

    @Override
    public String preview(CodeGeneratorPreviewForm dto) {
        return null;
    }
}

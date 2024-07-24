package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.CodeGeneratorConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.CodeGeneratorConstant;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableColumnVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableConfigVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.TableVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ICodeGeneratorConfigRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ICodeGeneratorService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.CodeGeneratorTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;


/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class CodeGeneratorServiceImpl
        extends BasicServiceImpl<ICodeGeneratorConfigRepository> implements ICodeGeneratorService {

    @Resource
    private CodeGeneratorTemplateService codeGeneratorTemplateService;

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
        CodeGeneratorConfigEntity config = baseRepository.getTableConfig(table);
        if (ObjUtil.isNotNull(config)) {
            TableConfigVO vo = new TableConfigVO();
            vo.setBasic(JSONUtil.parseObj(config.getBasic()).toBean(CodeBasic.class));
            vo.setFields(JSONUtil.parseArray(config.getFields()).toList(CodeField.class));
            vo.setInsertAndUpdate(JSONUtil.parseObj(config.getInsertAndUpdate()).toBean(CodeInsertAndUpdate.class));
            vo.setQueryFields(JSONUtil.parseArray(config.getQueryFields()).toList(CodeQueryField.class));
            vo.setTableFields(JSONUtil.parseArray(config.getTableFields()).toList(CodeTableField.class));
            vo.setDeleteInfo(JSONUtil.parseObj(config.getDeleteInfo()).toBean(CodeDelete.class));
            return vo;
        }
        return new TableConfigVO();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateConfig(CodeGeneratorConfigForm dto) {
        checkParam(dto);
        CodeGeneratorConfigEntity config = baseRepository.getTableConfig(dto.getTableName());
        if (ObjUtil.isNotNull(config)) {
            this.setFieldValue(dto, config);
            baseRepository.updateById(config);
            return;
        }
        config = new CodeGeneratorConfigEntity();
        this.setFieldValue(dto, config);
        config.setTableName(dto.getTableName());
        baseRepository.insert(config);
    }

    private void setFieldValue(CodeGeneratorConfigForm dto, CodeGeneratorConfigEntity config) {
        config.setBasic(JSONUtil.toJsonStr(dto.getBasic()));
        config.setFields(JSONUtil.toJsonStr(dto.getFields()));
        config.setInsertAndUpdate(JSONUtil.toJsonStr(dto.getInsertAndUpdate()));
        config.setQueryFields(JSONUtil.toJsonStr(dto.getQueryFields()));
        config.setTableFields(JSONUtil.toJsonStr(dto.getTableFields()));
        config.setDeleteInfo(JSONUtil.toJsonStr(dto.getDeleteInfo()));
    }

    private void checkParam(CodeGeneratorConfigForm dto) {
        List<TableColumnVO> tableColumns = getTableColumns(dto.getTableName());
        if (null != dto.getDeleteInfo() && dto.getDeleteInfo().getIsSupportDelete()
                && !dto.getDeleteInfo().getIsPhysicallyDeleted()) {
            Optional<TableColumnVO> any = tableColumns.stream()
                    .filter(e -> e.getColumnName().equals(CodeGeneratorConstant.DELETED_FLAG)).findAny();
            if (!any.isPresent()) {
                throw new BusinessException("table.must.have.delFlag.field");
            }
        }
        if(tableColumns.stream().noneMatch(e -> CodeGeneratorConstant.PRIMARY_KEY.equalsIgnoreCase(e.getColumnKey()))){
            throw new BusinessException("table.must.have.primary.key");
        }
    }

    @Override
    public String preview(CodeGeneratorPreviewForm dto) {
        String tableName = dto.getTableName();
        CodeGeneratorConfigEntity codeGeneratorConfigEntity = this.getConfigInfo(tableName);
        return codeGeneratorTemplateService.generate(dto.getTableName(), dto.getTemplateFile(), codeGeneratorConfigEntity);
    }

    @Override
    public byte[] download(String tableName) {
        CodeGeneratorConfigEntity configInfo = this.getConfigInfo(tableName);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        codeGeneratorTemplateService.zipGeneratedFiles(out, tableName, configInfo);
        return out.toByteArray();
    }

    private CodeGeneratorConfigEntity getConfigInfo(String tableName) {
        boolean existedByTable = baseRepository.existByTable(tableName);
        if (BooleanUtil.isFalse(existedByTable)) {
            this.throwDataNotExistException(tableName);
        }
        CodeGeneratorConfigEntity entity = baseRepository.getTableConfig(tableName);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(tableName);
        }
        List<TableColumnVO> columns = baseRepository.getByTable(tableName);
        if (CollUtil.isEmpty(columns)) {
            LogHelp.error(log, "表: {} 没有列信息无法生成", tableName);
            this.throwDataNotExistException(tableName);
        }
        return entity;
    }
}

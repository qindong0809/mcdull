package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.entity.CodeGeneratorConfigEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.CodeGeneratorConstant;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TableColumnVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TableConfigVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TableVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.CodeGeneratorConfigMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.ICodeGeneratorService;
import io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.CodeGeneratorTemplateService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * Code generator service
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class CodeGeneratorServiceImpl
        extends BasicCurdServiceImpl<CodeGeneratorConfigMapper, CodeGeneratorConfigEntity> implements ICodeGeneratorService {

    @Resource
    private CodeGeneratorTemplateService codeGeneratorTemplateService;

    @Override
    public List<TableColumnVO> getTableColumns(String table) {
       return this.getByTable(table);
    }

    @Override
    public PagedVO<TableVO> queryTableList(TableQueryForm dto) {
        Page<?> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        List<TableVO> tableVOList = this.queryTableList(page, dto);
        return PageUtil.toPage(tableVOList, page);
    }

    @Override
    public TableConfigVO getTableConfig(String table) {
        CodeGeneratorConfigEntity config = this.getTableConfigEntity(table);
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
        LogHelp.warn(logger, "table config is null, tableName: {}", table);
        return new TableConfigVO();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateConfig(CodeGeneratorConfigForm dto) {
        checkParam(dto);
        CodeGeneratorConfigEntity config = this.getTableConfigEntity(dto.getTableName());
        if (ObjUtil.isNotNull(config)) {
            this.setFieldValue(dto, config);
            super.updateById(config);
            return;
        }
        config = new CodeGeneratorConfigEntity();
        this.setFieldValue(dto, config);
        config.setTableName(dto.getTableName());
        baseMapper.insert(config);
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
        boolean existedByTable = this.existByTable(tableName);
        if (BooleanUtil.isFalse(existedByTable)) {
            LogicCheckUtil.throwDataNotExistException(tableName);
        }
        CodeGeneratorConfigEntity entity = this.getTableConfigEntity(tableName);
        if (ObjUtil.isNull(entity)) {
            LogicCheckUtil.throwDataNotExistException(tableName);
        }
        List<TableColumnVO> columns = this.getByTable(tableName);
        if (CollUtil.isEmpty(columns)) {
            LogHelp.error(logger, "表: {} 没有列信息无法生成", tableName);
            LogicCheckUtil.throwDataNotExistException(tableName);
        }
        return entity;
    }

    public List<CodeGeneratorConfigEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<CodeGeneratorConfigEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CodeGeneratorConfigEntity::getId, idList);
        List<CodeGeneratorConfigEntity> list =  baseMapper.selectList(wrapper);
        if (ObjectUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    public Page<CodeGeneratorConfigEntity> selectPage(FeedbackQueryDTO param) {
        LambdaQueryWrapper<CodeGeneratorConfigEntity> lambda = new QueryWrapper<CodeGeneratorConfigEntity>().lambda();
        String keyword = param.getKeyword();
        if (CharSequenceUtil.isNotBlank(keyword)) {
            lambda.like(CodeGeneratorConfigEntity::getTableName, keyword);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }


    public CodeGeneratorConfigEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }


    public void insert(CodeGeneratorConfigEntity entity) {
        baseMapper.insert(entity);
    }


    public boolean exist(CodeGeneratorConfigEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    public List<TableColumnVO> getByTable(String table) {
        return baseMapper.getByTable(table);
    }

    public List<TableVO> queryTableList(Page<?> page, TableQueryForm dto) {
        return baseMapper.queryTableList(page, dto);
    }

    public boolean existByTable(String tableName) {
        return baseMapper.existByTable(tableName) > 0;
    }

    public CodeGeneratorConfigEntity getTableConfigEntity(String tableName) {
        LambdaQueryWrapper<CodeGeneratorConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(CodeGeneratorConfigEntity::getTableName, tableName);
        return baseMapper.selectOne(query);
    }

    /**
     * 根据id删除批处理
     *
     * @param ids id集
     */
    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }

}

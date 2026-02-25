package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.audit.FormAudit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormRecordEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FormItemVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FormRecordDataVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FormVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.FormMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IFormManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFormRecordService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFormService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Form ServiceImpl
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FormServiceImpl
        extends BasicCurdServiceImpl<FormMapper, FormEntity> implements IFormService {

    @Resource
    private IFormManager formManager;

    @Resource
    private ICommonManager commonManager;

    @Resource
    private IAuditManager auditManager;

    @Resource
    private IFormRecordService formRecordService;

    @Override
    public PagedVO<FormVO> queryPage(FormQueryDTO dto) {
        List<FormVO> voList = new ArrayList<>();
        Page<FormEntity> entityPage = this.selectPage(dto);
        List<FormEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            List<FormRecordEntity> list = formRecordService.list();
            for (FormEntity entity : recordList) {
                FormVO vo = this.convertToVO(entity);
                Integer count = Convert.toInt(list.stream().filter(i -> i.getFormId().equals(entity.getId())).count());
                vo.setDataNumber(count);
                voList.add(vo);
            }
        }



        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(FormAddDTO dto) {
        FormEntity entity = this.getByName(dto.getName());
        if (ObjUtil.isNotNull(entity)) {
            LogicCheckUtil.throwDataExistException(dto.getName());
        }
        FormEntity formEntity = this.convertToEntity(dto);
        super.save(formEntity);
        auditManager.saveByAddEnum(formEntity.getName(), formEntity.getId(), this.buildAuditLog(formEntity));
    }

    private Audit buildAuditLog(FormEntity entity) {
        FormAudit audit = new FormAudit();
        audit.setName(entity.getName());
        audit.setJsonText(entity.getJsonText());
        audit.setPublish(entity.getPublish());
        audit.setRemark(entity.getRemark());
        return audit;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(FormUpdateDTO dto) {
        FormEntity tempForm = this.getByName(dto.getName());
        if (ObjUtil.isNotNull(tempForm)) {
            if (!tempForm.getId().equals(dto.getId())) {
                LogicCheckUtil.throwDataExistException(dto.getName());
            }
        }
        FormEntity oldEntity = super.getById(dto.getId());
        FormEntity newEntity = this.convertToEntity(dto);
        newEntity.setPublish(oldEntity.getPublish());
        newEntity.setId(dto.getId());
        super.updateById(newEntity);
        auditManager.saveByUpdateEnum(oldEntity.getName(), oldEntity.getId(),
                this.buildAuditLog(oldEntity), this.buildAuditLog(newEntity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        FormEntity entity = super.getById(id);
        if (ObjUtil.isNull(entity)) {
            LogicCheckUtil.throwDataNotExistException(id);
        }
        super.removeById(id);
        auditManager.saveByDeleteEnum(entity.getName(), entity.getId(), "");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateJsonText(FormUpdateJsonTextDTO dto) {
        FormEntity entity = super.getById(dto.getId());
        formManager.initFormAndFormItem(dto.getId(), dto.getJsonText());
        auditManager.saveByUpdateEnum(entity.getName(), entity.getId(),
                this.buildAuditLog(entity), this.buildAuditLog(super.getById(dto.getId())));
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void formConfigReady(Integer formId) {
        formManager.formConfigReady(formId);
    }

    @Override
    public FormVO detail(Integer formId) {
        FormEntity entity = super.getById(formId);
        if (ObjUtil.isNull(entity)) {
            LogicCheckUtil.throwDataNotExistException(formId);
        }
        return this.convertToVO(entity);
    }

    @Override
    public List<FormItemVO> itemConfigList(Integer formId) {
        FormEntity entity = super.getById(formId);
        if (ObjUtil.isNull(entity)) {
            LogicCheckUtil.throwDataNotExistException(formId);
        }
        if (BooleanUtil.isFalse(entity.getPublish())) {
            return Collections.emptyList();
        }
        String jsonText = entity.getJsonText();
        if (StrUtil.isBlank(jsonText)) {
            return Collections.emptyList();
        }

        List<JSONObject> formItemList = formManager.getFormItemList(jsonText);
        if (CollUtil.isEmpty(formItemList)) {
            return Collections.emptyList();
        }
        List<FormItemVO> list = new ArrayList<>();
        for (JSONObject jsonObject : formItemList) {
            if (ObjUtil.isNotNull(jsonObject)) {
                String title = jsonObject.get("label", String.class);
                String field = jsonObject.get("field", String.class);
                if (StrUtil.isAllNotBlank(title, field)) {
                    FormItemVO itemVO = new FormItemVO();
                    itemVO.setName(title);
                    itemVO.setKey(field);
                    list.add(itemVO);
                }
            }
        }
        return list;
    }

    @Override
    public void recordAdd(FormRecordAddDTO dto) {
        formManager.addFormRecordData(dto.getFormId(), dto.getFormData());
    }

    @Transactional(readOnly = true)
    @Override
    public PagedVO<Map<String, String>> recordQueryPage(FormRecordQueryDTO dto) {
        List<Map<String, String>> voList = this.getAllRecord(dto);
        return PageUtil.ofSub(voList, dto);
    }

    private List<Map<String, String>> getAllRecord(FormRecordQueryDTO dto) {
        List<FormRecordDataVO> list = formManager.recordList(dto.getFormId());
        List<Map<String, String>> voList = new ArrayList<>();
        for (FormRecordDataVO vo : list) {
            Map<String, String> itemMap = vo.getItemMap();
            String keyword = dto.getKeyword();
            boolean isContains = true;
            if (StrUtil.isNotBlank(keyword)) {
                isContains = false;
                boolean anyMatch = itemMap.entrySet().stream()
                        .anyMatch(i ->
                                StrUtil.contains(Convert.toStr(i.getValue(), StrUtil.EMPTY).toLowerCase(), keyword.toLowerCase()));
                if (anyMatch) {
                    isContains = true;
                }
            }
            if (isContains) {
                itemMap.put("id", vo.getId().toString());
                voList.add(itemMap);
            }
        }
        return voList;
    }

    @Override
    public void  exportData(FormRecordQueryDTO dto) {
        List<Map<String, String>> allRecord = this.getAllRecord(dto);
        List<FormItemVO> formItemVOS = this.itemConfigList(dto.getFormId());
        List<Pair<String, String>> pairList = new ArrayList<>();
        for (FormItemVO itemVO : formItemVOS) {
            pairList.add(Pair.of(itemVO.getName(), itemVO.getKey()));
        }
        FormEntity form = super.getById(dto.getFormId());
        String conditions = this.filterConditionsStr(dto);
        String sheetName = form.getName();
        commonManager.exportExcel(sheetName, conditions, pairList, allRecord);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteOneRecord(Integer recordId) {
        formManager.deleteOneRecord(recordId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOneRecord(FormRecordUpdateDTO dto) {
        formManager.updateOneRecord(dto.getRecordId(), dto.getFormData());
    }

    @Override
    public Map<String, Object> getOneRecordNoConvert(Integer recordId) {
        return formManager.getOneRecordNoConvert(recordId);
    }

    private String filterConditionsStr(FormRecordQueryDTO dto) {
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            return "关键字： " + keyword;
        }
        return StrUtil.EMPTY;
    }

    private FormEntity convertToEntity(FormAddDTO dto) {
        FormEntity formEntity = new FormEntity();
        formEntity.setName(dto.getName());
        formEntity.setRemark(dto.getRemark());
        return formEntity;
    }

    private FormVO convertToVO(FormEntity item){
        FormVO formVO = new FormVO();
        formVO.setId(item.getId());
        formVO.setName(item.getName());
        formVO.setJsonText(item.getJsonText());
        formVO.setPublish(item.getPublish());
        formVO.setRemark(item.getRemark());
        formVO.setCreateTime(item.getCreatedTime());
        formVO.setUpdateTime(item.getUpdatedTime());
        return formVO;
    }

    public Page<FormEntity> selectPage(FormQueryDTO param) {
        LambdaQueryWrapper<FormEntity> lambda = Wrappers.lambdaQuery();
        String keyword = param.getKeyword();
        if (ObjectUtil.isNotNull(keyword)) {
            lambda.like(FormEntity::getName, keyword);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    public FormEntity getByName(String name) {
        LambdaQueryWrapper<FormEntity> lambda = Wrappers.lambdaQuery();
        lambda.eq(FormEntity::getName, name);
        return baseMapper.selectOne(lambda);
    }

}

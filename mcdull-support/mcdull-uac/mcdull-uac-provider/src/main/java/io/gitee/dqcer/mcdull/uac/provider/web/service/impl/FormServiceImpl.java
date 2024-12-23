package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.audit.FormAudit;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormItemVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormRecordDataVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IFormManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Form ServiceImpl
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FormServiceImpl
        extends BasicServiceImpl<IFormRepository> implements IFormService {

    @Resource
    private IFormManager formManager;

    @Resource
    private ICommonManager commonManager;

    @Resource
    private IAuditManager auditManager;

    @Override
    public PagedVO<FormVO> queryPage(FormQueryDTO dto) {
        List<FormVO> voList = new ArrayList<>();
        Page<FormEntity> entityPage = baseRepository.selectPage(dto);
        List<FormEntity> recordList = entityPage.getRecords();
        if (CollUtil.isNotEmpty(recordList)) {
            for (FormEntity entity : recordList) {
                FormVO vo = this.convertToVO(entity);
                voList.add(vo);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(FormAddDTO dto) {
        FormEntity entity = baseRepository.getByName(dto.getName());
        if (ObjUtil.isNotNull(entity)) {
            this.throwDataExistException(dto.getName());
        }
        FormEntity formEntity = this.convertToEntity(dto);
        baseRepository.save(formEntity);
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
        FormEntity tempForm = baseRepository.getByName(dto.getName());
        if (ObjUtil.isNotNull(tempForm)) {
            if (!tempForm.getId().equals(dto.getId())) {
                this.throwDataExistException(dto.getName());
            }
        }
        FormEntity oldEntity = baseRepository.getById(dto.getId());
        FormEntity newEntity = this.convertToEntity(dto);
        newEntity.setPublish(oldEntity.getPublish());
        newEntity.setId(dto.getId());
        baseRepository.updateById(newEntity);
        auditManager.saveByUpdateEnum(oldEntity.getName(), oldEntity.getId(),
                this.buildAuditLog(oldEntity), this.buildAuditLog(newEntity));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        FormEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        baseRepository.removeById(id);
        auditManager.saveByDeleteEnum(entity.getName(), entity.getId(), "");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateJsonText(FormUpdateJsonTextDTO dto) {
        FormEntity entity = baseRepository.getById(dto.getId());
        formManager.initFormAndFormItem(dto.getId(), dto.getJsonText());
        auditManager.saveByUpdateEnum(entity.getName(), entity.getId(),
                this.buildAuditLog(entity), this.buildAuditLog(baseRepository.getById(dto.getId())));
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void formConfigReady(Integer formId) {
        formManager.formConfigReady(formId);
    }

    @Override
    public FormVO detail(Integer formId) {
        FormEntity entity = baseRepository.getById(formId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(formId);
        }
        return this.convertToVO(entity);
    }

    @Override
    public List<FormItemVO> itemConfigList(Integer formId) {
        FormEntity entity = baseRepository.getById(formId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(formId);
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
        Map<String, String> titleMap = new LinkedHashMap<>();
        for (FormItemVO itemVO : formItemVOS) {
            titleMap.put(itemVO.getName(), itemVO.getKey());
        }
        FormEntity form = baseRepository.getById(dto.getFormId());
        String conditions = this.filterConditionsStr(dto);
        String sheetName = form.getName();
        commonManager.exportExcel(sheetName, conditions, titleMap, allRecord);
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


}

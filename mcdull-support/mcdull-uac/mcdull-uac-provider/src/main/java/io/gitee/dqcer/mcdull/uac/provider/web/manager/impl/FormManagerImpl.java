package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.web.basic.GenericLogic;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormItemEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormRecordItemEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FormItemControlTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FormRecordDataVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormItemRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormRecordItemRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormRecordRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IFormManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *  动态表单通用逻辑实现类
 *
 * @author dqcer
 * @since 2022/12/24
 */
@Service
public class FormManagerImpl extends GenericLogic implements IFormManager {

    private static final Logger log = LoggerFactory.getLogger(FormManagerImpl.class);

    @Resource
    private IFormRepository formRepository;

    @Resource
    private IFormItemRepository formItemRepository;

    @Resource
    private IFormRecordRepository formRecordRepository;

    @Resource
    private IFormRecordItemRepository formRecordItemRepository;


    @Override
    public List<JSONObject> getFormItemList(String jsonText) {
        JSONObject jsonObject = JSONUtil.parseObj(jsonText);
        JSONArray objects = jsonObject.get("schemas", JSONArray.class);
        if (CollUtil.isEmpty(objects)) {
            return Collections.emptyList();
        }
        List<JSONObject> list = new ArrayList<>();
        for (Object object : objects) {
            if (ObjUtil.isNotNull(object)) {
                JSONObject subJsonObject = JSONUtil.parseObj(object);
                List<JSONObject> sub = extracted(subJsonObject);
                if (CollUtil.isNotEmpty(sub)) {
                    list.addAll(sub);
                }
            }
        }
        return list;
    }


    private static List<JSONObject> extracted(JSONObject jsonObject) {
        List<JSONObject> list = new ArrayList<>();
        Object value = jsonObject.get("children");
        if (ObjUtil.isNotNull(value)) {
            JSONArray objects = JSONUtil.parseArray(value);
            for (Object object : objects) {
                JSONObject subJsonObject = JSONUtil.parseObj(object);
                List<JSONObject> sub = extracted(subJsonObject);
                if (CollUtil.isNotEmpty(sub)) {
                    list.addAll(sub);
                }
            }
        } else {
           list.add(jsonObject);
        }
        return list;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initFormAndFormItem(Integer formId, String jsonText) {
        FormEntity entity = formRepository.getById(formId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(formId);
        }
        if (BooleanUtil.isTrue(entity.getPublish())) {
            throw new RuntimeException("动态表单已发布，不能修改");
        }
        entity.setJsonText(jsonText);
        formRepository.updateById(entity);

        formItemRepository.deleteByFormId(formId);

        List<JSONObject> formItemList = this.getFormItemList(jsonText);
        if (CollUtil.isEmpty(formItemList)) {
            return;
        }
        List<FormItemEntity> itemEntityList = new ArrayList<>();
        int i = 0;
        for (JSONObject jsonObject : formItemList) {
            if (ObjUtil.isNotNull(jsonObject)) {
                String title = jsonObject.get("label", String.class);
                String field = jsonObject.get("field", String.class);
                if (StrUtil.isAllNotBlank(title, field)) {
                    FormItemEntity formItem = new FormItemEntity();
                    formItem.setFormId(formId);
                    formItem.setLabel(title);
                    formItem.setControlType(jsonObject.get("type", String.class));
                    formItem.setLabelCode(field);
                    JSONObject componentProps = jsonObject.get("componentProps", JSONObject.class);
                    if (ObjUtil.isNotNull(componentProps)) {
                        JSONArray objects = componentProps.get("options", JSONArray.class);
                        if (ObjUtil.isNotNull(objects)) {
                            formItem.setOptions(objects.toString());
                        }
                    }
                    formItem.setOrderNumber(i ++);
                    JSONArray jsonArray = jsonObject.get("rules", JSONArray.class);
                    if (CollUtil.isNotEmpty(jsonArray)) {
                        for (Object o : jsonArray) {
                            JSONObject rules = JSONUtil.parseObj(o);
                            if (ObjUtil.isNotNull(rules)) {
                                Boolean required = rules.get("required", Boolean.class);
                                if (BooleanUtil.isTrue(required)) {
                                    formItem.setRequired(true);
                                }
                            }
                        }
                    }

                    itemEntityList.add(formItem);
                }
            }
        }
        formItemRepository.saveBatch(itemEntityList);
    }

    @Override
    public void addFormRecordData(Integer formId, Map<String, String> formDataMap) {
        FormEntity formEntity = formRepository.getById(formId);
        if (ObjUtil.isNull(formEntity)) {
            this.throwDataNotExistException(formId);
        }
        if (BooleanUtil.isFalse(formEntity.getPublish())) {
            throw new RuntimeException("动态表单未发布，不能添加数据");
        }
        if (MapUtil.isEmpty(formDataMap)) {
            return;
        }
        List<FormItemEntity> formItemEntityList = formItemRepository.selectByFormId(formId);
        if (CollUtil.isEmpty(formItemEntityList)) {
            return;
        }
        FormRecordEntity recordEntity = new FormRecordEntity();
        recordEntity.setFormId(formId);
        formRecordRepository.save(recordEntity);

        Integer recordId = recordEntity.getId();
        List<FormRecordItemEntity> recordItemList = new ArrayList<>();
        formItemEntityList.forEach(formItemEntity -> {
            String labelCode = formItemEntity.getLabelCode();
            String value = formDataMap.get(labelCode);
            FormRecordItemEntity recordItem = new FormRecordItemEntity();
            recordItem.setFormItemId(formItemEntity.getId());
            recordItem.setFormRecordId(recordId);
            recordItem.setFormId(formId);
            recordItem.setCurrentValue(value);
            recordItemList.add(recordItem);
        });
        formRecordItemRepository.saveBatch(recordItemList);
    }

    @Override
    public List<FormRecordDataVO> recordList(Integer formId) {
        if (ObjUtil.isNull(formId)) {
            return Collections.emptyList();
        }
        if (BooleanUtil.isFalse(formRepository.getById(formId).getPublish())) {
            return Collections.emptyList();
        }
        List<FormItemEntity> itemList = formItemRepository.selectByFormId(formId);
        if (CollUtil.isEmpty(itemList)) {
            return Collections.emptyList();
        }
        Map<Integer, FormItemEntity> itemMap = itemList.stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        List<FormRecordEntity> recordList = formRecordRepository.selectByFormId(formId);
        if (CollUtil.isEmpty(recordList)) {
            return Collections.emptyList();
        }
        Map<Integer, FormRecordEntity> recordMap = recordList.stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        List<FormRecordItemEntity> recordItemList1 = formRecordItemRepository.selectByFormId(formId);
        if (CollUtil.isEmpty(recordItemList1)) {
            return Collections.emptyList();
        }
        List<FormRecordItemEntity> statusOkList = recordItemList1.stream()
                .filter(i -> recordMap.containsKey(i.getFormRecordId())).collect(Collectors.toList());
        if (CollUtil.isEmpty(statusOkList)) {
            return Collections.emptyList();
        }
        List<FormRecordDataVO> result = new ArrayList<>();
        Map<Integer, List<FormRecordItemEntity>> recordGroupMap = statusOkList.stream()
                .collect(Collectors.groupingBy(FormRecordItemEntity::getFormRecordId));
        for (Map.Entry<Integer, List<FormRecordItemEntity>> entry : recordGroupMap.entrySet()) {
            FormRecordDataVO dataVO = new FormRecordDataVO();
            dataVO.setId(entry.getKey());
            dataVO.setCreateTime(LocalDateTimeUtil.of(recordMap.get(entry.getKey()).getCreatedTime()));
            dataVO.setUpdateTime(LocalDateTimeUtil.of(recordMap.get(entry.getKey()).getUpdatedTime()));
            Map<String, String> dataMap = MapUtil.newHashMap();
            entry.getValue().forEach(formRecordItemEntity -> {
                FormItemEntity item = itemMap.get(formRecordItemEntity.getFormItemId());
                if (ObjUtil.isNotNull(item)) {
                    String currentValue = formRecordItemEntity.getCurrentValue();
                    String controlType = item.getControlType();
                    FormItemControlTypeEnum typeEnum = IEnum.getByCode(FormItemControlTypeEnum.class, controlType);
                    if (FormItemControlTypeEnum.INPUT.equals(typeEnum)
                            ||  FormItemControlTypeEnum.TEXTAREA.equals(typeEnum)
                            ||  FormItemControlTypeEnum.DATE.equals(typeEnum)
                            ||  FormItemControlTypeEnum.DATETIME.equals(typeEnum)
                            ||  FormItemControlTypeEnum.NUMBER.equals(typeEnum)
                            ||  FormItemControlTypeEnum.TIME.equals(typeEnum)
                    ) {
                        dataMap.put(item.getLabelCode(), currentValue);
                    }
                    if (FormItemControlTypeEnum.SELECT.equals(typeEnum)
                            || FormItemControlTypeEnum.RADIO.equals(typeEnum)
                            || FormItemControlTypeEnum.CHECKBOX.equals(typeEnum) ) {
                        String options = item.getOptions();
                        if (StrUtil.isNotBlank(options)) {
                            JSONArray objects = JSONUtil.parseArray(options);
                            if (ObjUtil.isNotNull(objects)) {
                                List<LabelValueVO> list = objects.toList(LabelValueVO.class);
                                if (CollUtil.isNotEmpty(list)) {
                                    LabelValueVO<?, ?> vo = list.stream()
                                            .filter(i -> i.getValue().equals(currentValue))
                                            .findFirst()
                                            .orElse(null);
                                    if (ObjUtil.isNotNull(vo)) {
                                        dataMap.put(item.getLabelCode(), Convert.toStr(vo.getLabel()));
                                    }
                                }
                            }
                        }
                    }
                }
            });
            dataVO.setItemMap(dataMap);
            result.add(dataVO);
        }
        return result;
    }

    @Override
    public void formConfigReady(Integer formId) {
        FormEntity form = formRepository.getById(formId);
        if (ObjUtil.isNull(form)) {
            this.throwDataNotExistException(formId);
        }
        if (BooleanUtil.isTrue(form.getPublish())) {
            return;
        }
        form.setPublish(true);
        formRepository.updateById(form);
    }

    @Override
    public void deleteOneRecord(Integer recordId) {
        FormRecordEntity formRecord = formRecordRepository.getById(recordId);
        if (ObjUtil.isNull(formRecord)) {
            this.throwDataNotExistException(recordId);
        }
       formRecordRepository.removeById(recordId);
    }

    @Override
    public void updateOneRecord(Integer recordId, Map<String, String> formDataMap) {
        if (MapUtil.isEmpty(formDataMap)) {
            LogHelp.warn(log, "formDataMap is empty");
            return;
        }
        FormRecordEntity formRecord = formRecordRepository.getById(recordId);
        if (ObjUtil.isNull(formRecord)) {
            this.throwDataNotExistException(recordId);
        }
        List<FormItemEntity> formItemEntityList = formItemRepository.selectByFormId(formRecord.getFormId());
        if (CollUtil.isEmpty(formItemEntityList)) {
            LogHelp.warn(log, "formId:{}. formItemEntityList is empty", formRecord.getFormId());
            return;
        }
        Map<Integer, FormItemEntity> itemMap = formItemEntityList.stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));

        List<FormRecordItemEntity> list = formRecordItemRepository.selectByRecordId(recordId);
        if (CollUtil.isEmpty(list)) {
            LogHelp.error(log, "recordId:{} is not exist", recordId);
            return;
        }
        list.forEach(recordItem -> {
            Integer formItemId = recordItem.getFormItemId();
            FormItemEntity formItem = itemMap.get(formItemId);
            if (ObjUtil.isNull(formItem)) {
                LogHelp.error(log, "formItemId:{} is not exist", formItemId);
                return;
            }
            String labelCode = formItem.getLabelCode();
            String currentValue = formDataMap.get(labelCode);
            recordItem.setCurrentValue(currentValue);
        });
        formRecordItemRepository.updateBatchById(list);
    }

    @Override
    public Map<String, Object> getOneRecordNoConvert(Integer recordId) {
        FormRecordEntity formRecord = formRecordRepository.getById(recordId);
        if (ObjUtil.isNull(formRecord)) {
            return Collections.emptyMap();
        }
        Integer formId = formRecord.getFormId();
        List<FormItemEntity> itemList = formItemRepository.selectByFormId(formId);
        if (CollUtil.isEmpty(itemList)) {
            return Collections.emptyMap();
        }
        Map<Integer, FormItemEntity> itemMap = itemList.stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));

        List<FormRecordItemEntity> recordItemList = formRecordItemRepository.selectByRecordId(recordId);
        if (CollUtil.isNotEmpty(recordItemList)) {
            Map<String, Object> result = MapUtil.newHashMap();
            recordItemList.forEach(recordItem -> {
                FormItemEntity formItem = itemMap.get(recordItem.getFormItemId());
                if (ObjUtil.isNull(formItem)) {
                    return;
                }
                String labelCode = formItem.getLabelCode();
                String currentValue = recordItem.getCurrentValue();
                FormItemControlTypeEnum typeEnum = IEnum.getByCode(FormItemControlTypeEnum.class,
                        formItem.getControlType());
                if (FormItemControlTypeEnum.NUMBER.equals(typeEnum)) {
                    result.put(labelCode, Convert.toNumber(currentValue));
                }else {
                    result.put(labelCode, currentValue);
                }
            });
            return result;
        }
        return Collections.emptyMap();
    }
}

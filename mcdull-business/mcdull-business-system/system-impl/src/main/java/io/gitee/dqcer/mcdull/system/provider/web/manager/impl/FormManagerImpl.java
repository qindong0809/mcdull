package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.web.basic.GenericLogic;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormItemEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormRecordEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormRecordItemEntity;
import io.gitee.dqcer.mcdull.system.facade.model.enums.FormItemControlTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FormRecordDataVO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IFormManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFormItemService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFormRecordItemService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFormRecordService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFormService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private IFormService formService;
    @Resource
    private IFormItemService formItemService;
    @Resource
    private IFormRecordService formRecordService;
    @Resource
    private IFormRecordItemService formRecordItemService;


    @Override
    public List<JSONObject> getFormItemList(String jsonText) {
        if (StrUtil.isBlank(jsonText)) {
            return Collections.emptyList();
        }
        JSONObject jsonObject = JSON.parseObject(jsonText);
        JSONArray objects = jsonObject.getJSONArray("schemas");
        if (CollUtil.isEmpty(objects)) {
            return Collections.emptyList();
        }
        List<JSONObject> list = new ArrayList<>();
        for (Object object : objects) {
            if (ObjUtil.isNotNull(object)) {
                JSONObject subJsonObject = (JSONObject) object;
                List<JSONObject> sub = extractLeafNodes(subJsonObject);
                if (CollUtil.isNotEmpty(sub)) {
                    list.addAll(sub);
                }
            }
        }
        return list;
    }


    private static List<JSONObject> extractLeafNodes(JSONObject jsonObject) {
        List<JSONObject> list = new ArrayList<>();
        JSONArray objects = jsonObject.getJSONArray("children");
        if (ObjUtil.isNotNull(objects)) {
            for (Object object : objects) {
                JSONObject subJsonObject = (JSONObject) object;
                List<JSONObject> sub = extractLeafNodes(subJsonObject);
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
        FormEntity entity = formService.getById(formId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(formId);
        }
        if (BooleanUtil.isTrue(entity.getPublish())) {
            throw new BusinessException("动态表单已发布，不能修改");
        }
        entity.setJsonText(jsonText);
        formService.updateById(entity);

        formItemService.deleteByFormId(formId);

        List<JSONObject> formItemList = this.getFormItemList(jsonText);
        if (CollUtil.isEmpty(formItemList)) {
            return;
        }
        List<FormItemEntity> itemEntityList = new ArrayList<>();
        int i = 0;
        for (JSONObject jsonObject : formItemList) {
            if (ObjUtil.isNotNull(jsonObject)) {
                String title = jsonObject.getString("label");
                String field = jsonObject.getString("field");
                if (StrUtil.isAllNotBlank(title, field)) {
                    FormItemEntity formItem = new FormItemEntity();
                    formItem.setFormId(formId);
                    formItem.setLabel(title);
                    formItem.setControlType(jsonObject.getString("type"));
                    formItem.setLabelCode(field);
                    JSONObject componentProps = jsonObject.getJSONObject("componentProps");
                    if (ObjUtil.isNotNull(componentProps)) {
                        JSONArray objects = componentProps.getJSONArray("options");
                        if (ObjUtil.isNotNull(objects)) {
                            formItem.setOptions(objects.toString());
                        } else {
                            formItem.setOptions(componentProps.toString());
                        }
                    }
                    formItem.setOrderNumber(i ++);
                    JSONArray jsonArray = jsonObject.getJSONArray("rules");
                    if (CollUtil.isNotEmpty(jsonArray)) {
                        for (Object o : jsonArray) {
                            JSONObject rules = (JSONObject) o;
                            if (ObjUtil.isNotNull(rules)) {
                                Boolean required = rules.getBoolean("required");
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
        formItemService.saveBatch(itemEntityList, itemEntityList.size());
    }

    @Override
    public void addFormRecordData(Integer formId, Map<String, Object> formDataMap) {
        FormEntity formEntity = formService.getById(formId);
        if (ObjUtil.isNull(formEntity)) {
            this.throwDataNotExistException(formId);
        }
        if (BooleanUtil.isFalse(formEntity.getPublish())) {
            throw new BusinessException("动态表单未发布，不能添加数据");
        }
        if (MapUtil.isEmpty(formDataMap)) {
            return;
        }
        List<FormItemEntity> formItemEntityList = formItemService.selectByFormId(formId);
        if (CollUtil.isEmpty(formItemEntityList)) {
            return;
        }
        FormRecordEntity recordEntity = new FormRecordEntity();
        recordEntity.setFormId(formId);
        formRecordService.save(recordEntity);

        Integer recordId = recordEntity.getId();
        List<FormRecordItemEntity> recordItemList = new ArrayList<>();
        formItemEntityList.forEach(formItemEntity -> {
            String labelCode = formItemEntity.getLabelCode();
            Object obj = formDataMap.get(labelCode);
            FormRecordItemEntity recordItem = new FormRecordItemEntity();
            recordItem.setFormItemId(formItemEntity.getId());
            recordItem.setFormRecordId(recordId);
            recordItem.setFormId(formId);
            FormItemControlTypeEnum controlTypeEnum = IEnum.getByCode(FormItemControlTypeEnum.class,
                    formItemEntity.getControlType());
            if (FormItemControlTypeEnum.CHECKBOX.equals(controlTypeEnum)) {
                if (obj != null) {
                    recordItem.setCurrentValue(toJsonArrayString(obj));
                }
            } else {
                recordItem.setCurrentValue(Convert.toStr(obj));
            }
            recordItemList.add(recordItem);
        });
        formRecordItemService.saveBatch(recordItemList, recordItemList.size());
    }

    @Override
    public List<FormRecordDataVO> recordList(Integer formId) {
        if (ObjUtil.isNull(formId)) {
            return Collections.emptyList();
        }
        if (BooleanUtil.isFalse(formService.getById(formId).getPublish())) {
            return Collections.emptyList();
        }
        List<FormItemEntity> itemList = formItemService.selectByFormId(formId);
        if (CollUtil.isEmpty(itemList)) {
            return Collections.emptyList();
        }
        Map<Integer, FormItemEntity> itemMap = itemList.stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        List<FormRecordEntity> recordList = formRecordService.selectByFormId(formId);
        if (CollUtil.isEmpty(recordList)) {
            return Collections.emptyList();
        }
        Map<Integer, FormRecordEntity> recordMap = recordList.stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        List<FormRecordItemEntity> recordItemList1 = formRecordItemService.selectByFormId(formId);
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
                    if (FormItemControlTypeEnum.SELECT.equals(typeEnum)
                            || FormItemControlTypeEnum.RADIO.equals(typeEnum)) {
                        String options = item.getOptions();
                        if (StrUtil.isNotBlank(options)) {
                            JSONArray objects = JSON.parseArray(options);
                            if (ObjUtil.isNotNull(objects)) {
                                List<LabelValueVO> list = objects.toJavaList(LabelValueVO.class);
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
                    }  else if (FormItemControlTypeEnum.SWITCH.equals(typeEnum)){
                        String options = item.getOptions();
                        if (StrUtil.isNotBlank(options)) {
                            JSONObject jsonObject = JSON.parseObject(options);
                            if (ObjUtil.isNotNull(jsonObject)) {
                                String checkedValue = jsonObject.getString("checkedValue");
                                String unCheckedValue = jsonObject.getString("unCheckedValue");
                                if (StrUtil.isNotBlank(checkedValue) && StrUtil.isNotBlank(unCheckedValue)) {
                                    if (checkedValue.equals(currentValue)) {
                                        dataMap.put(item.getLabelCode(), Convert.toStr(jsonObject.get("checkedChildren")));
                                    } else {
                                        dataMap.put(item.getLabelCode(), Convert.toStr(jsonObject.get("unCheckedChildren")));
                                    }
                                }
                            }
                            if (StrUtil.isNotBlank(jsonObject.getString(currentValue))) {
                                dataMap.put(item.getLabelCode(), jsonObject.getString(currentValue));
                            }
                        }

                    } else if (FormItemControlTypeEnum.CHECKBOX.equals(typeEnum)){
                        String options = item.getOptions();
                        if (StrUtil.isNotBlank(options)) {
                            JSONArray objects = JSON.parseArray(options);
                            if (ObjUtil.isNotNull(objects)) {
                                List<LabelValueVO> list = objects.toJavaList(LabelValueVO.class);
                                if (CollUtil.isNotEmpty(list)) {
                                    if (StrUtil.isNotBlank(currentValue)) {
                                        JSONArray parsedArray = JSON.parseArray(currentValue);
                                        if (CollUtil.isNotEmpty(parsedArray)) {
                                            List<String> checkboxValueList = parsedArray.toJavaList(String.class);
                                            if (CollUtil.isNotEmpty(checkboxValueList)) {
                                                String checkboxValueJoin = list.stream()
                                                        .filter(i -> checkboxValueList.contains(Convert.toStr(i.getValue())))
                                                        .map(i->Convert.toStr(i.getLabel()))
                                                        .collect(Collectors.joining(", "));
                                                dataMap.put(item.getLabelCode(), checkboxValueJoin);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        dataMap.put(item.getLabelCode(), currentValue);
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
        FormEntity form = formService.getById(formId);
        if (ObjUtil.isNull(form)) {
            this.throwDataNotExistException(formId);
        }
        if (BooleanUtil.isTrue(form.getPublish())) {
            return;
        }
        form.setPublish(true);
        formService.updateById(form);
    }

    @Override
    public void deleteOneRecord(Integer recordId) {
        FormRecordEntity formRecord = formRecordService.getById(recordId);
        if (ObjUtil.isNull(formRecord)) {
            this.throwDataNotExistException(recordId);
        }
        formRecordService.removeById(recordId);
    }

    @Override
    public void updateOneRecord(Integer recordId, Map<String, Object> formDataMap) {
        if (MapUtil.isEmpty(formDataMap)) {
            LogHelp.warn(log, "formDataMap is empty");
            return;
        }
        FormRecordEntity formRecord = formRecordService.getById(recordId);
        if (ObjUtil.isNull(formRecord)) {
            this.throwDataNotExistException(recordId);
        }
        List<FormItemEntity> formItemEntityList = formItemService.selectByFormId(formRecord.getFormId());
        if (CollUtil.isEmpty(formItemEntityList)) {
            LogHelp.warn(log, "formId:{}. formItemEntityList is empty", formRecord.getFormId());
            return;
        }
        Map<Integer, FormItemEntity> itemMap = formItemEntityList.stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));

        List<FormRecordItemEntity> list = formRecordItemService.selectByRecordId(recordId);
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
            Object obj = formDataMap.get(labelCode);
            FormItemControlTypeEnum controlTypeEnum = IEnum.getByCode(FormItemControlTypeEnum.class,
                    formItem.getControlType());
            if (FormItemControlTypeEnum.CHECKBOX.equals(controlTypeEnum)) {
                if (obj != null) {
                    recordItem.setCurrentValue(toJsonArrayString(obj));
                }
            } else {
                recordItem.setCurrentValue(Convert.toStr(obj));
            }
        });
        formRecordItemService.updateBatchById(list, list.size());
    }

    @Override
    public Map<String, Object> getOneRecordNoConvert(Integer recordId) {
        FormRecordEntity formRecord = formRecordService.getById(recordId);
        if (ObjUtil.isNull(formRecord)) {
            return Collections.emptyMap();
        }
        Integer formId = formRecord.getFormId();
        List<FormItemEntity> itemList = formItemService.selectByFormId(formId);
        if (CollUtil.isEmpty(itemList)) {
            return Collections.emptyMap();
        }
        Map<Integer, FormItemEntity> itemMap = itemList.stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));

        List<FormRecordItemEntity> recordItemList = formRecordItemService.selectByRecordId(recordId);
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

    /**
     * 复选框值可能已是集合，也可能是 JSON 字符串，统一存成 JSON 数组字符串
     */
    private static String toJsonArrayString(Object value) {
        if (value instanceof CharSequence) {
            return JSON.parseArray(value.toString()).toString();
        }
        return JSON.toJSONString(value);
    }
}

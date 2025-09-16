package io.gitee.dqcer.mcdull.business.common.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Getter
@Slf4j
public class DataAnalysisListener extends AnalysisEventListener<Map<Integer, String>> {

    @Setter
    private Map<Integer, String> headMap;

    @Setter
    private List<Tuple> errorList;

    @Setter
    private List<Tuple> dataList;

    private final List<DynamicFieldTemplate> formFieldList;

    public DataAnalysisListener(List<DynamicFieldTemplate> formFieldList) {
        this.formFieldList = formFieldList;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        boolean headMapIsOk = this.validateHeadMap(headMap);
        if (!headMapIsOk) {
            log.error("validate error");
        }
        this.headMap = headMap;
    }

    private boolean validateHeadMap(Map<Integer, String> headMap) {
        for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
            String headValue = entry.getValue();
            boolean anyMatch = formFieldList.stream().anyMatch(field -> field.getName().equals(headValue));
            if (!anyMatch) {
                return false;
            }
        }
        return true;
    }

    private Map<Integer, DynamicFieldTemplate> transformHeaderMapToFieldMap(Map<Integer, String> headMap) {
        Map<Integer, DynamicFieldTemplate> newHeadMap = new HashMap<>();
        for (DynamicFieldTemplate field : formFieldList) {
            String name = field.getName();
            Optional<Map.Entry<Integer, String>> first = headMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(name)).findFirst();
            first.ifPresent(integerStringEntry -> newHeadMap.put(integerStringEntry.getKey(), field));
        }
        return newHeadMap;
    }



    @Override
    public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {
        if (MapUtil.isEmpty(headMap) || MapUtil.isEmpty(integerStringMap)) {
            return;
        }
        Map<Integer, DynamicFieldTemplate> dynamicFormFieldMap = this.transformHeaderMapToFieldMap(headMap);
        if (MapUtil.isEmpty(dynamicFormFieldMap)) {
            return;
        }
        List<Pair<DynamicFieldTemplate, String>> oneRowDataList = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : integerStringMap.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            DynamicFieldTemplate dynamicFieldTemplate = dynamicFormFieldMap.get(key);
            if (dynamicFieldTemplate == null) {
                continue;
            }
            oneRowDataList.add(new Pair<>(dynamicFieldTemplate, value));
            dataList.add(new Tuple(oneRowDataList));
        }

    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        super.extra(extra, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (CollUtil.isNotEmpty(this.dataList)) {
            List<Tuple> removeList = new ArrayList<>();
            for (Tuple tuple : dataList) {
                List<Pair<DynamicFieldTemplate, String>> oneRowDataList = tuple.get(0);
                if (CollUtil.isNotEmpty(oneRowDataList)) {
                    for (Pair<DynamicFieldTemplate, String> pair : oneRowDataList) {
                        boolean validate = this.validate(pair.getKey(), pair.getValue());
                        if (!validate) {
                            removeList.add(tuple);
                        }
                    }
                }
            }
            if (CollUtil.isNotEmpty(removeList)) {
                this.dataList.removeAll(removeList);
            }
        }
    }

    private boolean validate(DynamicFieldTemplate fieldTemplate, String value) {
        if (fieldTemplate.getRequired() != null && fieldTemplate.getRequired()) {
            if (CharSequenceUtil.isBlank(value)) {
                errorList.add(new Tuple(fieldTemplate.getName(), "不能为空"));
                return false;
            }
        }
        FormItemControlTypeEnum controlTypeEnum = fieldTemplate.getControlTypeEnum();
        if (controlTypeEnum != null) {
            if (controlTypeEnum.equals(FormItemControlTypeEnum.NUMBER)) {
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    value = "0";
                }
            }
            if (controlTypeEnum.equals(FormItemControlTypeEnum.DATE)
                    || controlTypeEnum.equals(FormItemControlTypeEnum.DATETIME)) {
            }
        }
        return true;
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return super.hasNext(context);
    }
}

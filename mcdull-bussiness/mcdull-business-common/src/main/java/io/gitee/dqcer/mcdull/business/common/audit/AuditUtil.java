package io.gitee.dqcer.mcdull.business.common.audit;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.SymbolConstants;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author dqcer
 */
public class AuditUtil {

    public static <T extends Audit> List<FieldDiff> compare(T after) {
        JSONObject beforeJson = JSONUtil.parseObj(after, false);
        List<FieldDiff> fieldDiffList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : beforeJson) {
            String key = entry.getKey();
            Object afterValue = entry.getValue();
            Field field = ReflectUtil.getField(after.getClass(), key);
            AuditDescription annotation = field.getAnnotation(AuditDescription.class);
            if (null != annotation) {
                FieldDiff diff = getFieldDiff(annotation, afterValue);
                fieldDiffList.add(diff);
            }
        }
        CollUtil.sort(fieldDiffList, (o1, o2) -> NumberUtil.compare(o1.description.sort(), o2.description.sort()));
        return fieldDiffList;
    }

    public static <T extends Audit> List<FieldDiff> compare(T before, T after) {
        JSONObject beforeJson = JSONUtil.parseObj(before, false);
        JSONObject afterJson = JSONUtil.parseObj(after, false);

        List<FieldDiff> fieldDiffList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : beforeJson) {
            String key = entry.getKey();
            Object beforeValue = entry.getValue();
            Object afterValue = afterJson.get(key);
            if (!Objects.equals(afterValue, beforeValue)) {
                Field field = ReflectUtil.getField(before.getClass(), key);
                AuditDescription annotation = field.getAnnotation(AuditDescription.class);
                if (null != annotation) {
                    FieldDiff diff = getFieldDiff(annotation, afterValue);
                    diff.setBeforeValue(Convert.toStr(beforeValue));
                    fieldDiffList.add(diff);
                }
            }
        }
        CollUtil.sort(fieldDiffList, (o1, o2) -> NumberUtil.compare(o1.description.sort(), o2.description.sort()));
        return fieldDiffList;
    }

    private static FieldDiff getFieldDiff(AuditDescription annotation, Object afterValue) {
        FieldDiff diff = new FieldDiff();
        diff.setDescription(annotation);
        diff.setAfterValue(Convert.toStr(afterValue));
        diff.setFieldName(annotation.label());
        diff.setSortOrder(annotation.sort());
        return diff;
    }

    public static <T extends Audit> String compareStr(T before) {
        List<FieldDiff> list = compare(before);
        StringBuilder builder = new StringBuilder();
        builder.append(before.prefix());
        builder.append(before.tagCharacter()[0]);
        StringJoiner result = new StringJoiner(SymbolConstants.COMMA + SymbolConstants.SPACE);
        for (FieldDiff diff : list) {
            Object beforeValue = diff.getBeforeValue();
            StringBuilder one = new StringBuilder();
            AuditDescription description = diff.getDescription();
            boolean dateType = beforeValue instanceof Date;
            String val = dateType ? DateUtil.format((Date) beforeValue, description.datePattern())
                    : Convert.toStr(beforeValue, StrUtil.EMPTY);
            if (StrUtil.isNotBlank(val)) {
                one.append(StrUtil.format("{}{}", description.label(), before.separate()));
                one.append(StrUtil.format("{}{}{}",
                        description.tagCharacter()[0],
                        val,
                        description.tagCharacter()[1]));
                result.add(one);
            }
        }
        builder.append(result);
        builder.append(before.tagCharacter()[1]);
        return builder.toString();
    }

    public static <T extends Audit> String compareStr(List<FieldDiff> list, boolean update) {
        StringJoiner result = new StringJoiner(SymbolConstants.COMMA + SymbolConstants.SPACE);
        list.sort((o1, o2) -> NumberUtil.compare(o1.getSortOrder(), o2.getSortOrder()));
        for (FieldDiff diff : list) {
            StringBuilder one = new StringBuilder();
            if (update) {
                one.append(StrUtil.format("{}: '{}' {} '{}'",
                        diff.getFieldName(), diff.getBeforeValue(), "更新为", diff.getAfterValue()));
            } else {
                one.append(StrUtil.format("{}: '{}'", diff.getFieldName(), diff.getBeforeValue()));
            }
            result.add(one);
        }
        return result.toString();
    }

    public static <T extends Audit> String compareStr(T before, T after) {
        List<FieldDiff> list = compare(before, after);
        StringBuilder builder = new StringBuilder();
        builder.append(before.prefix());
        builder.append(before.tagCharacter()[0]);
        StringJoiner result = new StringJoiner(SymbolConstants.COMMA + SymbolConstants.SPACE);

        for (FieldDiff diff : list) {
            Object afterValue = diff.getAfterValue();
            Object beforeValue = diff.getBeforeValue();
            if (ObjectUtil.notEqual(beforeValue, afterValue)) {
                StringBuilder one = new StringBuilder();
                AuditDescription description = diff.getDescription();
                one.append(StrUtil.format("{}{}", description.label(), before.separate()));
                one.append(StrUtil.format("{}{}{}",
                        description.tagCharacter()[0],
                        Convert.toStr(beforeValue, StrUtil.EMPTY),
                        description.tagCharacter()[1]));
                one.append(description.to());
                one.append(StrUtil.format("{}{}{}",
                        description.tagCharacter()[0],
                        Convert.toStr(afterValue, StrUtil.EMPTY),
                        description.tagCharacter()[1]));

                result.add(one);
            }
        }
        builder.append(result);
        builder.append(before.tagCharacter()[1]);
        return builder.toString();
    }


    @Setter
    @Getter
    public static class FieldDiff {

        private AuditDescription description;
        private String beforeValue;
        private String afterValue;
        private String fieldName;
        private Integer sortOrder;

    }
}

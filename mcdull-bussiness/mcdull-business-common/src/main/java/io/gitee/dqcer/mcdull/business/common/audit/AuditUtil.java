package io.gitee.dqcer.mcdull.business.common.audit;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.SymbolConstants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author dqcer
 */
public class AuditUtil {


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
                if (annotation == null) {
                    throw new IllegalArgumentException();
                }
                FieldDiff diff = new FieldDiff();
                diff.setField(field);
                diff.setDescription(annotation);
                diff.setBeforeValue(beforeValue);
                diff.setAfterValue(afterValue);
                fieldDiffList.add(diff);
            }
        }
        return fieldDiffList;
    }

    public static <T extends Audit> String compareStr(T before, T after) {
        List<FieldDiff> list = compare(before, after);
        StringBuilder builder = new StringBuilder();
        builder.append(before.prefix());
        builder.append(before.tagCharacter()[0]);
        boolean hashDiff = false;
        for (FieldDiff diff : list) {
            Object afterValue = diff.getAfterValue();
            Object beforeValue = diff.getBeforeValue();
            if (ObjectUtil.notEqual(beforeValue, afterValue)) {
                if (hashDiff) {
                    builder.append(SymbolConstants.COMMA).append(SymbolConstants.SPACE);
                }
                AuditDescription description = diff.getDescription();
                builder.append(description.label());
                builder.append(before.separate());
                builder.append(Convert.toStr(beforeValue));
                builder.append(description.to());
                builder.append(Convert.toStr(afterValue));
                hashDiff = true;
            }
        }
        builder.append(before.tagCharacter()[1]);
        return builder.toString();
    }


    public static class FieldDiff {

        private Field field;

        private AuditDescription description;
        private Object beforeValue;

        private Object afterValue;

        public AuditDescription getDescription() {
            return description;
        }

        public void setDescription(AuditDescription description) {
            this.description = description;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public Object getBeforeValue() {
            return beforeValue;
        }

        public void setBeforeValue(Object beforeValue) {
            this.beforeValue = beforeValue;
        }

        public Object getAfterValue() {
            return afterValue;
        }

        public void setAfterValue(Object afterValue) {
            this.afterValue = afterValue;
        }
    }
}

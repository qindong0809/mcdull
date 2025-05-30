package io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.backend.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeField;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeTableField;
import io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;

/**
 */

public class VOVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();

        Map<String, CodeField> fieldMap = getFieldMap(form);
        List<CodeTableField> updateFieldList = form.getTableFields().stream().filter(e -> Boolean.TRUE.equals(e.getShowFlag())).collect(Collectors.toList());

        ImmutablePair<List<String>, List<Map<String, Object>>> packageListAndFields = getPackageListAndFields(updateFieldList, form);

        variablesMap.put("packageName", form.getBasic().getJavaPackageName() + ".domain.vo");
        variablesMap.put("importPackageList", packageListAndFields.getLeft());
        variablesMap.put("fields", packageListAndFields.getRight());

        return variablesMap;
    }

    public ImmutablePair<List<String>, List<Map<String, Object>>> getPackageListAndFields(List<CodeTableField> fields, CodeGeneratorConfigForm form) {
        if (CollUtil.isEmpty(fields)) {
            return ImmutablePair.of(new ArrayList<>(), new ArrayList<>());
        }

        Map<String, CodeField> fieldMap = getFieldMap(form);
        HashSet<String> packageList = new HashSet<>();


        /**
         * 1、LocalDate、LocalDateTime、BigDecimal 类型的包名
         * 2、排序
         */

        List<Map<String, Object>> finalFieldList = new ArrayList<>();

        for (CodeTableField field : fields) {
            CodeField codeField = fieldMap.get(field.getColumnName());
            if (codeField == null) {
                continue;
            }

            // CodeField 和 CodeTableField 合并
            Map<String, Object> finalFieldMap = BeanUtil.beanToMap(field);
            finalFieldMap.putAll(BeanUtil.beanToMap(codeField));

            // 枚举
            if (StrUtil.isNotEmpty(codeField.getEnumName())) {
                packageList.add("import net.lab1024.sa.base.common.doc.SchemaEnum;");
                packageList.add("import " + form.getBasic().getJavaPackageName() + ".constant." + codeField.getEnumName() + ";");

                finalFieldMap.put("apiModelProperty", "@SchemaEnum(value = " + codeField.getEnumName() + ".class, desc = \"" + codeField.getLabel() + "\")");
                finalFieldMap.put("isEnum", true);

            } else {
                String apiModelProperty = "@Schema(description = \"" + codeField.getLabel() + "\")";
                finalFieldMap.put("apiModelProperty", apiModelProperty);

                packageList.add("import io.doc.v3.oas.annotations.media.Schema;");
            }


            //字典
            if (isDict(field.getColumnName(), form)) {
                finalFieldMap.put("dict", "\n    @JsonDeserialize(using = DictValueVoDeserializer.class)");
                packageList.add("import com.fasterxml.jackson.databind.annotation.JsonDeserialize;");
                packageList.add("import net.lab1024.sa.base.common.json.deserializer.DictValueVoDeserializer;");
            }

            //文件上传
            if (isFile(field.getColumnName(), form)) {
                finalFieldMap.put("file", "\n    @JsonDeserialize(using = FileKeyVoDeserializer.class)");
                packageList.add("import com.fasterxml.jackson.databind.annotation.JsonDeserialize;");
                packageList.add("import io.gitee.dqcer.mcdull.uac.config.io.gitee.dqcer.mcdull.system.provider.FileKeyVoDeserializer;");
            }

            packageList.add(getJavaPackageName(codeField.getJavaType()));
            finalFieldList.add(finalFieldMap);
        }


        // lombok
        packageList.add("import lombok.Data;");

        List<String> packageNameList = packageList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Collections.sort(packageNameList);
        return ImmutablePair.of(packageNameList, finalFieldList);
    }

}

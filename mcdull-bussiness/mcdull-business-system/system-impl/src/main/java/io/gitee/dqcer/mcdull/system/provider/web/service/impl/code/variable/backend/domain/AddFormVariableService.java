package io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.backend.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeField;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeInsertAndUpdate;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeInsertAndUpdateField;
import io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;

/**
 */

public class AddFormVariableService extends CodeGenerateBaseVariableService {


    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        CodeInsertAndUpdate insertAndUpdate = form.getInsertAndUpdate();
        return insertAndUpdate != null && insertAndUpdate.getIsSupportInsertAndUpdate() != null && insertAndUpdate.getIsSupportInsertAndUpdate();
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();

        List<CodeInsertAndUpdateField> updateFieldList = form.getInsertAndUpdate().getFieldList().stream().filter(e -> Boolean.TRUE.equals(e.getInsertFlag())).collect(Collectors.toList());
        ImmutablePair<List<String>, List<Map<String, Object>>> packageListAndFields = getPackageListAndFields(updateFieldList, form);

        variablesMap.put("packageName", form.getBasic().getJavaPackageName() + ".domain.form");
        variablesMap.put("importPackageList", packageListAndFields.getLeft());
        variablesMap.put("fields", packageListAndFields.getRight());

        return variablesMap;
    }


    public ImmutablePair<List<String>, List<Map<String, Object>>> getPackageListAndFields(List<CodeInsertAndUpdateField> fields, CodeGeneratorConfigForm form) {
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

        for (CodeInsertAndUpdateField field : fields) {
            CodeField codeField = fieldMap.get(field.getColumnName());
            if (codeField == null) {
                continue;
            }

            // CodeField 和 InsertAndUpdateField 合并
            Map<String, Object> finalFieldMap = BeanUtil.beanToMap(field);
            finalFieldMap.putAll(BeanUtil.beanToMap(codeField));

            // 枚举
            if (StrUtil.isNotEmpty(codeField.getEnumName())) {
                packageList.add("import net.lab1024.sa.base.common.doc.SchemaEnum;");
                packageList.add("import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;");
                packageList.add("import " + form.getBasic().getJavaPackageName() + ".constant." + codeField.getEnumName() + ";");

                //enum check
                String checkEnumPrefix = "@CheckEnum(value = " + codeField.getEnumName() + ".class, message = \"" + codeField.getLabel() + " 错误\"";
                String checkEnum = checkEnumPrefix + (field.getRequiredFlag() ? ", required = true)" : ")");

                finalFieldMap.put("apiModelProperty", "@SchemaEnum(value = " + codeField.getEnumName() + ".class, desc = \"" + codeField.getLabel() + "\")");
                finalFieldMap.put("checkEnum", checkEnum);
                finalFieldMap.put("isEnum", true);

            } else {
                String prefix = "@Schema(description = \"" + codeField.getLabel() + "\"";
                String apiModelProperty = prefix + (field.getRequiredFlag() ? ", requiredMode = Schema.RequiredMode.REQUIRED)" : ")");
                finalFieldMap.put("apiModelProperty", apiModelProperty);

                packageList.add("import io.doc.v3.oas.annotations.media.Schema;");

                if (Boolean.TRUE.equals(field.getRequiredFlag())) {
                    String notEmptyPrefix = "String".equals(codeField.getJavaType()) ? "@NotBlank" : "@NotNull";
                    finalFieldMap.put("notEmpty", "\n    " + notEmptyPrefix + "(message = \"" + codeField.getLabel() + " 不能为空\")");
                    packageList.add("String".equals(codeField.getJavaType()) ? "import jakarta.validation.constraints.NotBlank;"
                            : "import jakarta.validation.constraints.NotNull;");
                }
            }


            //字典
            if (StrUtil.isNotEmpty(codeField.getDict())) {
                finalFieldMap.put("dict", "\n    @JsonDeserialize(using = DictValueVoDeserializer.class)");
                packageList.add("import com.fasterxml.jackson.databind.annotation.JsonDeserialize;");
                packageList.add("import net.lab1024.sa.base.common.json.deserializer.DictValueVoDeserializer;");
            }

            //文件上传
            if (StrUtil.contains(field.getFrontComponent(), "Upload")) {
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

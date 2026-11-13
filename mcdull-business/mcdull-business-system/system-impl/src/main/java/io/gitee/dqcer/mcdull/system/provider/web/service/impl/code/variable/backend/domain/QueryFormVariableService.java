package io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.backend.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeField;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeQueryField;
import io.gitee.dqcer.mcdull.system.provider.model.enums.CodeQueryFieldQueryTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Collectors;

/**

 */

public class QueryFormVariableService extends CodeGenerateBaseVariableService {


    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();
        ImmutablePair<List<String>, List<Map<String, Object>>> packageListAndFields = getPackageListAndFields(form);
        variablesMap.put("packageName", form.getBasic().getJavaPackageName() + ".domain.form");
        variablesMap.put("importPackageList", packageListAndFields.getLeft());
        variablesMap.put("fields", packageListAndFields.getRight());
        return variablesMap;
    }


    public ImmutablePair<List<String>, List<Map<String, Object>>> getPackageListAndFields(CodeGeneratorConfigForm form) {
        List<CodeQueryField> fields = form.getQueryFields();
        if (CollUtil.isEmpty(fields)) {
            return ImmutablePair.of(new ArrayList<>(), new ArrayList<>());
        }

        HashSet<String> packageList = new HashSet<>();


        /**
         * 1、LocalDate、LocalDateTime、BigDecimal 类型的包名
         * 2、排序
         */

        List<Map<String, Object>> finalFieldList = new ArrayList<>();

        for (CodeQueryField field : fields) {

            // CodeField 和 InsertAndUpdateField 合并
            Map<String, Object> finalFieldMap = BeanUtil.beanToMap(field);
            finalFieldMap.putAll(BeanUtil.beanToMap(field));

            String queryTypeEnumStr = field.getQueryTypeEnum();

            CodeQueryFieldQueryTypeEnum queryTypeEnum = IEnum.getByCode(CodeQueryFieldQueryTypeEnum.class, queryTypeEnumStr);
            if (queryTypeEnum == null) {
                continue;
            }

            String apiModelProperty = "@Schema(description = \"" + field.getLabel() + "\")";
            finalFieldMap.put("apiModelProperty", apiModelProperty);
            packageList.add("import io.doc.v3.oas.annotations.media.Schema;");

            CodeField codeField = null;

            switch (queryTypeEnum) {
                case LIKE:
                    finalFieldMap.put("javaType", "String");
                    break;
                case EQUAL:
                    codeField = getCodeFieldByColumnName(field.getColumnNameList().get(0), form);
                    if (codeField == null) {
                        finalFieldMap.put("javaType", "String");
                    } else {
                        finalFieldMap.put("javaType", codeField.getJavaType());
                    }
                    break;
                case DATE_RANGE:
                case DATE:
                    packageList.add("import java.time.LocalDate;");
                    finalFieldMap.put("javaType", "LocalDate");
                    break;
                case ENUM:
                    codeField = getCodeFieldByColumnName(field.getColumnNameList().get(0), form);
                    if (codeField == null) {
                        continue;
                    }

                    packageList.add("import net.lab1024.sa.base.common.doc.SchemaEnum;");
                    packageList.add("import net.lab1024.sa.base.common.validator.enumeration.CheckEnum;");
                    packageList.add("import " + form.getBasic().getJavaPackageName() + ".constant." + codeField.getEnumName() + ";");

                    //enum check
                    String checkEnum = "@CheckEnum(value = " + codeField.getEnumName() + ".class, message = \"" + codeField.getLabel() + " 错误\")";
                    finalFieldMap.put("apiModelProperty", "@SchemaEnum(value = " + codeField.getEnumName() + ".class, desc = \"" + codeField.getLabel() + "\")");
                    finalFieldMap.put("checkEnum", checkEnum);
                    finalFieldMap.put("isEnum", true);

                    finalFieldMap.put("javaType", codeField.getJavaType());
                    break;
                default:
                    finalFieldMap.put("javaType", "String");
            }

            finalFieldList.add(finalFieldMap);
        }


        // lombok
        packageList.add("import lombok.Data;");

        List<String> packageNameList = packageList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Collections.sort(packageNameList);
        return ImmutablePair.of(packageNameList, finalFieldList);
    }

}

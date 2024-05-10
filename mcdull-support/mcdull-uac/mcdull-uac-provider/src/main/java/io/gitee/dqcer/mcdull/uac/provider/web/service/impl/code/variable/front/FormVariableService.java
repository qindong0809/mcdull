package io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.front;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.CaseFormat;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeField;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeInsertAndUpdateField;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.CodeFrontComponentEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;

import java.util.*;

/**
 */

public class FormVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();
        List<Map<String, Object>> fieldsVariableList = new ArrayList<>();
        List<CodeInsertAndUpdateField> fieldList = form.getInsertAndUpdate().getFieldList();

        HashSet<String> frontImportSet = new HashSet<>();

        for (CodeInsertAndUpdateField field : fieldList) {
            // 不存在 添加 和 更新
            if (!(field.getInsertFlag() || field.getUpdateFlag())) {
                continue;
            }

            Map<String, Object> objectMap = BeanUtil.beanToMap(field);

            CodeField codeField = getCodeFieldByColumnName(field.getColumnName(), form);
            if (codeField == null) {
                continue;
            }
            objectMap.put("label", codeField.getLabel());
            objectMap.put("fieldName", codeField.getFieldName());
            objectMap.put("dict", codeField.getDict());

            if (StrUtil.isNotBlank(codeField.getEnumName())) {
                String upperUnderscoreEnum = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, codeField.getEnumName());
                objectMap.put("upperUnderscoreEnum", upperUnderscoreEnum);
            }

            fieldsVariableList.add(objectMap);

            if (CodeFrontComponentEnum.ENUM_SELECT.getCode().equals(field.getFrontComponent())) {
                frontImportSet.add("import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';");
            }

            if (CodeFrontComponentEnum.BOOLEAN_SELECT.getCode().equals(field.getFrontComponent())) {
                frontImportSet.add("import BooleanSelect from '/@/components/framework/boolean-select/index.vue';");
            }

            if (CodeFrontComponentEnum.DICT_SELECT.getCode().equals(field.getFrontComponent())) {
                frontImportSet.add("import DictSelect from '/@/components/support/dict-select/index.vue';");
            }

            if (CodeFrontComponentEnum.FILE_UPLOAD.getCode().equals(field.getFrontComponent())) {
                frontImportSet.add("import FileUpload from '/@/components/support/file-upload/index.vue';");
            }
        }

        variablesMap.put("formFields", fieldsVariableList);
        variablesMap.put("frontImportList", new ArrayList<>(frontImportSet));

        return variablesMap;
    }
}

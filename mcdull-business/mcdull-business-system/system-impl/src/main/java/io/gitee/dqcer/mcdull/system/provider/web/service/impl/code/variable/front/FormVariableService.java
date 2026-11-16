package io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.front;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeFieldDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeInsertAndUpdateFieldDTO;
import io.gitee.dqcer.mcdull.system.provider.model.enums.CodeFrontComponentEnum;
import io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;

import java.util.*;

/**
 */

public class FormVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigDTO form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigDTO form) {
        Map<String, Object> variablesMap = new HashMap<>();
        List<Map<String, Object>> fieldsVariableList = new ArrayList<>();
        List<CodeInsertAndUpdateFieldDTO> fieldList = form.getInsertAndUpdate().getFieldList();

        HashSet<String> frontImportSet = new HashSet<>();

        for (CodeInsertAndUpdateFieldDTO field : fieldList) {
            // 不存在 添加 和 更新
            if (!(field.getInsertFlag() || field.getUpdateFlag())) {
                continue;
            }

            Map<String, Object> objectMap = BeanUtil.beanToMap(field);

            CodeFieldDTO codeField = getCodeFieldByColumnName(field.getColumnName(), form);
            if (codeField == null) {
                continue;
            }
            objectMap.put("label", codeField.getLabel());
            objectMap.put("fieldName", codeField.getFieldName());
            objectMap.put("dict", codeField.getDict());

            if (StrUtil.isNotBlank(codeField.getEnumName())) {
//                String upperUnderscoreEnum = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, codeField.getEnumName());
                String upperUnderscoreEnum = NamingCase.toUnderlineCase(codeField.getEnumName());
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

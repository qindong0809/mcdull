package io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.front;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeQueryField;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.CodeQueryFieldQueryTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;

import java.util.*;

/**
 */

public class ListVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();

        List<Map<String, Object>> variableList = new ArrayList<>();
        List<CodeQueryField> queryFields = form.getQueryFields();
        HashSet<String> frontImportSet = new HashSet<>();
//        frontImportSet.add("import " + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, form.getBasic().getModuleName()) + "Form from './" + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, form.getBasic().getModuleName()) + "-form.vue';");
        frontImportSet.add("import " + StrUtil.lowerFirst(form.getBasic().getModuleName()) + "Form from './" + NamingCase.toKebabCase(form.getBasic().getModuleName()) + "-form.vue';");

        for (CodeQueryField queryField : queryFields) {
            Map<String, Object> objectMap = BeanUtil.beanToMap(queryField);
            variableList.add(objectMap);

            if("Enum".equals(queryField.getQueryTypeEnum())){
                frontImportSet.add("import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';");
            }

            if("Dict".equals(queryField.getQueryTypeEnum())){
                frontImportSet.add("import DictSelect from '/@/components/support/dict-select/index.vue';");
            }

            if(CodeQueryFieldQueryTypeEnum.DATE_RANGE.getCode().equals(queryField.getQueryTypeEnum())){
                frontImportSet.add("import { defaultTimeRanges } from '/@/lib/default-time-ranges';");
            }

        }
        variablesMap.put("queryFields",variableList);
        variablesMap.put("frontImportList",new ArrayList<>(frontImportSet));
        return variablesMap;
    }
}

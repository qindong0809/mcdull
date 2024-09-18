package io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.front;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeField;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */

public class ConstVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();
        List<Map<String, Object>> enumList = new ArrayList<>();
        List<CodeField> enumFiledList = form.getFields().stream().filter(e -> StrUtil.isNotBlank(e.getEnumName())).collect(Collectors.toList());
        for (CodeField codeField : enumFiledList) {
            Map<String, Object> beanToMap = BeanUtil.beanToMap(codeField);
//            String upperUnderscoreEnum = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, codeField.getEnumName());
            String upperUnderscoreEnum = NamingCase.toUnderlineCase(codeField.getEnumName());
            beanToMap.put("upperUnderscoreEnum", upperUnderscoreEnum);
            enumList.add(beanToMap);
        }
        variablesMap.put("enumList", enumList);
        return variablesMap;
    }
}

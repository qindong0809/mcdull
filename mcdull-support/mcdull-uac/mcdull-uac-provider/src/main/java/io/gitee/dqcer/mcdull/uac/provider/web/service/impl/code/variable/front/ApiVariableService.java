package io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.front;


import io.gitee.dqcer.mcdull.uac.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;

import java.util.HashMap;
import java.util.Map;

/**
 */

public class ApiVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();

        return variablesMap;
    }
}

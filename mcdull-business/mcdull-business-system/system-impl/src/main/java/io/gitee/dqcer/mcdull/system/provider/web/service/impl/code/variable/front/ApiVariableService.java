package io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.front;


import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigDTO;
import io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;

import java.util.HashMap;
import java.util.Map;

/**
 */

public class ApiVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigDTO form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigDTO form) {
        Map<String, Object> variablesMap = new HashMap<>();

        return variablesMap;
    }
}

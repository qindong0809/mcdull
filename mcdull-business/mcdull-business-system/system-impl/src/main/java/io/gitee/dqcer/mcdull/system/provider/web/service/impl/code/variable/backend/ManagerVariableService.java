package io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.backend;


import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeInsertAndUpdateFieldDTO;
import io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;

import java.util.*;
import java.util.stream.Collectors;

/**
 */

public class ManagerVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigDTO form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigDTO form) {
        Map<String, Object> variablesMap = new HashMap<>();

        List<CodeInsertAndUpdateFieldDTO> updateFieldList = form.getInsertAndUpdate().getFieldList().stream().filter(e -> Boolean.TRUE.equals(e.getInsertFlag())).collect(Collectors.toList());
        List<String> packageList = getPackageList(updateFieldList, form);

        variablesMap.put("packageName", form.getBasic().getJavaPackageName() + ".manager" );
        variablesMap.put("importPackageList", packageList);

        return variablesMap;
    }


    public List<String> getPackageList(List<CodeInsertAndUpdateFieldDTO> fields, CodeGeneratorConfigDTO form) {
        if (CollUtil.isEmpty(fields)) {
            return new ArrayList<>();
        }

        HashSet<String> packageList = new HashSet<>();

        //1、javabean相关的包
        packageList.addAll(getJavaBeanImportClass(form).stream().filter(e -> e.contains("Entity;")).collect(Collectors.toList()));

        //2、dao
        packageList.add("import " + form.getBasic().getJavaPackageName() + ".dao."+ form.getBasic().getModuleName() + "Dao;" );
        return new ArrayList<>(packageList);
    }

}

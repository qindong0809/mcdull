package io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.backend;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeInsertAndUpdateField;
import io.gitee.dqcer.mcdull.system.provider.model.enums.CodeDeleteEnum;
import io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;

import java.util.*;
import java.util.stream.Collectors;

/**
 */

public class ControllerVariableService extends CodeGenerateBaseVariableService {

    @Override
    public boolean isSupport(CodeGeneratorConfigForm form) {
        return true;
    }

    @Override
    public Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form) {
        Map<String, Object> variablesMap = new HashMap<>();

        List<CodeInsertAndUpdateField> updateFieldList = form.getInsertAndUpdate().getFieldList().stream().filter(e -> Boolean.TRUE.equals(e.getInsertFlag())).collect(Collectors.toList());

        variablesMap.put("packageName", form.getBasic().getJavaPackageName() + ".controller");

        List<String> packageList = getPackageList(updateFieldList, form);
        variablesMap.put("importPackageList", packageList);

        return variablesMap;
    }


    public List<String> getPackageList(List<CodeInsertAndUpdateField> fields, CodeGeneratorConfigForm form) {
        if (CollUtil.isEmpty(fields)) {
            return new ArrayList<>();
        }

        HashSet<String> packageSet = new HashSet<>();

        //1、javabean相关的包
        packageSet.addAll(getJavaBeanImportClass(form).stream().filter(e -> !e.contains("Entity;")).collect(Collectors.toList()));

        //2、其他包
        if (form.getDeleteInfo().getIsSupportDelete()) {

            CodeDeleteEnum codeDeleteEnum = IEnum.getByCode(CodeDeleteEnum.class, form.getDeleteInfo().getDeleteEnum());
            if (codeDeleteEnum == CodeDeleteEnum.BATCH || codeDeleteEnum == CodeDeleteEnum.SINGLE_AND_BATCH) {
                //2、批量删除的话，要导入ValidateList
                packageSet.add("import java.util.List;");
            }

            if (codeDeleteEnum == CodeDeleteEnum.SINGLE || codeDeleteEnum == CodeDeleteEnum.SINGLE_AND_BATCH) {
                //3、单个删除的话，要导入 @PathVariable
                packageSet.add("import org.springframework.web.bind.annotation.PathVariable;");
                packageSet.add("import org.springframework.web.bind.annotation.GetMapping;");
            }
        }

//        packageSet.add("import " + form.getBasic().getJavaPackageName() + ".service." + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, form.getBasic().getModuleName()) + "Service;");
        packageSet.add("import " + form.getBasic().getJavaPackageName() + ".service." + StrUtil.upperFirst( form.getBasic().getModuleName()) + "Service;");

        // 排序一下
        ArrayList<String> packageList = new ArrayList<>(packageSet);
        Collections.sort(packageList);
        return packageList;
    }

}

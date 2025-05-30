package io.gitee.dqcer.mcdull.system.provider.web.service.impl.code.variable;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeField;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeGeneratorConfigForm;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeInsertAndUpdate;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CodeInsertAndUpdateField;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 */
public abstract class CodeGenerateBaseVariableService {

    public abstract Map<String, Object> getInjectVariablesMap(CodeGeneratorConfigForm form);

    /**
     * 是否支持 :
     * 1、增加、修改
     * 2、删除
     *
     * @param form
     * @return
     */
    public abstract boolean isSupport(CodeGeneratorConfigForm form);

    /**
     * 获取所有javabean的 import 包名
     *
     * @param form
     * @return
     */
    public List<String> getJavaBeanImportClass(CodeGeneratorConfigForm form) {
//        String upperCamelName = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_CAMEL, form.getBasic().getModuleName());
        String upperCamelName = StrUtil.upperFirst(form.getBasic().getModuleName());
        ArrayList<String> list = new ArrayList<>();

        list.add("import " + form.getBasic().getJavaPackageName() + ".domain.entity." + upperCamelName + "Entity;" );

        list.add("import " + form.getBasic().getJavaPackageName() + ".domain.form." + upperCamelName + "AddForm;" );
        list.add("import " + form.getBasic().getJavaPackageName() + ".domain.form." + upperCamelName + "UpdateForm;" );
        list.add("import " + form.getBasic().getJavaPackageName() + ".domain.form." + upperCamelName + "QueryForm;" );

        list.add("import " + form.getBasic().getJavaPackageName() + ".domain.vo." + upperCamelName + "VO;" );
        return list;
    }


    /**
     * 根据列名查找 CodeField
     */
    public CodeField getCodeFieldByColumnName(String columnName, CodeGeneratorConfigForm form) {
        List<CodeField> fields = form.getFields();
        if (CollUtil.isEmpty(fields)) {
            return null;
        }

        return fields.stream().filter(e -> columnName.equals(e.getColumnName()))
                .findFirst().get();
    }


    /**
     * 是否为文件上传字段
     */
    protected boolean isFile(String columnName, CodeGeneratorConfigForm form) {
        CodeInsertAndUpdate insertAndUpdate = form.getInsertAndUpdate();
        if (insertAndUpdate == null) {
            return false;
        }

        List<CodeInsertAndUpdateField> fieldList = insertAndUpdate.getFieldList();
        if (CollUtil.isEmpty(fieldList)) {
            return false;
        }

        Optional<CodeInsertAndUpdateField> first = fieldList.stream().filter(e -> columnName.equals(e.getColumnName())).findFirst();
        if (!first.isPresent()) {
            return false;
        }

        CodeInsertAndUpdateField field = first.get();
        return StrUtil.contains(field.getFrontComponent(), "Upload" );
    }

    /**
     * 是否为 枚举
     */
    protected boolean isDict(String columnName, CodeGeneratorConfigForm form) {
        List<CodeField> fields = form.getFields();
        if (CollUtil.isEmpty(fields)) {
            return false;
        }

        Optional<CodeField> first = fields.stream().filter(e -> columnName.equals(e.getColumnName())).findFirst();
        if (first.isPresent()) {
            return false;
        }

        CodeField codeField = first.get();
        return codeField.getDict() != null;
    }

    /**
     * 是否为 枚举
     */
    protected boolean isEnum(String columnName, CodeGeneratorConfigForm form) {
        List<CodeField> fields = form.getFields();
        if (CollUtil.isEmpty(fields)) {
            return false;
        }

        Optional<CodeField> first = fields.stream().filter(e -> columnName.equals(e.getColumnName())).findFirst();
        if (first.isPresent()) {
            return false;
        }

        CodeField codeField = first.get();
        return codeField.getEnumName() != null;
    }

    /**
     * 获取字段集合
     *
     * @param form
     * @return
     */
    protected Map<String, CodeField> getFieldMap(CodeGeneratorConfigForm form) {
        List<CodeField> fields = form.getFields();
        if (fields == null) {
            return new HashMap<>();
        }

        return fields.stream().collect(Collectors.toMap(CodeField::getColumnName, Function.identity()));
    }

    /**
     * 获取java类型
     *
     * @return
     */
    protected String getJavaPackageName(String javaType) {
        if ("BigDecimal".equals(javaType)) {
            return "import java.math.BigDecimal;";
        } else if ("LocalDate".equals(javaType)) {
            return "import java.time.LocalDate;";
        } else if ("LocalDateTime".equals(javaType)) {
            return "import java.time.LocalDateTime;";
        } else {
            return null;
        }
    }

}

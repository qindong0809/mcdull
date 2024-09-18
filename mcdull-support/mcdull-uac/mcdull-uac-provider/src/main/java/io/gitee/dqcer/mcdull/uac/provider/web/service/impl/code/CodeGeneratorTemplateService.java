package io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.CodeGeneratorConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.util.CodeGeneratorTool;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.CodeGenerateBaseVariableService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.backend.*;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.backend.domain.*;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.front.ApiVariableService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.front.ConstVariableService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.front.FormVariableService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.impl.code.variable.front.ListVariableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码生成器 模板 Service
 *
 */

@Service
@Slf4j
public class CodeGeneratorTemplateService {


    private Map<String, CodeGenerateBaseVariableService> map = new HashMap<>();

    @PostConstruct
    public void init() {
        // 后端
        map.put("java/domain/entity/Entity.java", new EntityVariableService());
        map.put("java/domain/form/AddDTO.java", new AddFormVariableService());
        map.put("java/domain/form/UpdateDTO.java", new UpdateFormVariableService());
        map.put("java/domain/form/QueryDTO.java", new QueryFormVariableService());
        map.put("java/domain/vo/VO.java", new VOVariableService());
        map.put("java/controller/Controller.java", new ControllerVariableService());
        map.put("java/service/impl/ServiceImpl.java", new ServiceVariableService());
        map.put("java/service/IService.java", new ServiceVariableService());
        map.put("java/dao/repository/impl/RepositoryImpl.java", new RepositoryImplVariableService());
        map.put("java/dao/repository/IRepository.java", new RepositoryVariableService());
//        map.put("java/manager/Manager.java", new ManagerVariableService());
        map.put("java/dao/mapper/Mapper.java", new DaoVariableService());
        map.put("java/mapper/Mapper.xml", new MapperVariableService());
        // 前端
        map.put("js/api.js", new ApiVariableService());
        map.put("js/const.js", new ConstVariableService());
        map.put("js/list.vue", new ListVariableService());
        map.put("js/form.vue", new FormVariableService());
    }

    public void zipGeneratedFiles(OutputStream outputStream, String tableName,
                                  CodeGeneratorConfigEntity codeGeneratorConfigEntity) {
        String uuid = UUID.randomUUID().toString();
        File dir = new File(uuid);

        // 1、生产文件
        CodeBasic basic = JSONUtil.toBean(codeGeneratorConfigEntity.getBasic(), CodeBasic.class);
        String moduleName = basic.getModuleName();

        for (Map.Entry<String, CodeGenerateBaseVariableService> entry : map.entrySet()) {
            try {
                String templateFile = entry.getKey();
                String upperCamel = new CodeGeneratorTool().lowerCamel2UpperCamel(moduleName);
                String lowerHyphen = new CodeGeneratorTool().lowerCamel2LowerHyphen(moduleName);
                String[] templateSplit = templateFile.split("/");
                String fileName = templateFile.startsWith("java") ? upperCamel + templateSplit[templateSplit.length - 1] : lowerHyphen + "-" + templateSplit[templateSplit.length - 1];
                String fullPathFileName = templateFile.replaceAll(templateSplit[templateSplit.length - 1], fileName);
                fullPathFileName = fullPathFileName.replaceAll("java/", "java/" + basic.getModuleName().toLowerCase() + "/");

                String fileContent = generate(tableName, templateFile, codeGeneratorConfigEntity);
                File file = new File(uuid + "/" + fullPathFileName);
                file.getParentFile().mkdirs();
                FileUtil.appendUtf8String(fileContent, file);
            } catch (IORuntimeException e) {
                log.error(e.getMessage(), e);
            }
        }

        // 2、后端的枚举文件
        List<CodeField> fields = JSONUtil.toList(codeGeneratorConfigEntity.getFields(), CodeField.class);
        if (CollUtil.isNotEmpty(fields)) {
            List<CodeField> enumFiledList = fields.stream().filter(e -> StrUtil.isNotBlank(e.getEnumName())).collect(Collectors.toList());
            for (CodeField codeField : enumFiledList) {
                Map<String, Object> variablesMap = new HashMap<>();

//                String enumName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, codeField.getEnumName());
                String enumName = StrUtil.upperFirst(codeField.getEnumName());
                if (!enumName.endsWith("Enum")) {
                    enumName = enumName + "Enum";
                }
                variablesMap.put("enumName", enumName);
                variablesMap.put("enumDesc", codeField.getColumnComment());
                variablesMap.put("enumJavaType", codeField.getJavaType());
                variablesMap.put("basic", basic);
                variablesMap.put("packageName", basic.getJavaPackageName() + ".constant");

                String fileContent = render("code-generator-template/java/constant/enum.java.vm", variablesMap);
                File file = new File(uuid + "/java/" + basic.getModuleName().toLowerCase() + "/constant/" + enumName + ".java");
                file.getParentFile().mkdirs();
                FileUtil.appendUtf8String(fileContent, file);
            }
        }


        ZipUtil.zip(outputStream, StandardCharsets.UTF_8, false, null, dir);

        FileUtil.del(dir);

    }


    public String generate(String tableName, String file, CodeGeneratorConfigEntity codeGeneratorConfigEntity) {

        // -------------------- 1 校验不支持的代码生成，比如增加、删除等 --------------------

        String finalFile = file;
        Optional<String> optional = map.keySet().stream().filter(e -> e.contains(finalFile)).findFirst();
        if (!optional.isPresent()) {
            return "不存在此模板！";
        }

        file = optional.get();
        CodeGenerateBaseVariableService codeGenerateBaseVariableService = map.get(file);
        if (codeGenerateBaseVariableService == null) {
            return "代码生成Service不存在，请检查相关代码！";
        }

        CodeBasic basic = JSONUtil.toBean(codeGeneratorConfigEntity.getBasic(), CodeBasic.class);
        List<CodeField> fields = JSONUtil.toList(codeGeneratorConfigEntity.getFields(), CodeField.class);
        CodeInsertAndUpdate insertAndUpdate = JSONUtil.toBean(codeGeneratorConfigEntity.getInsertAndUpdate(), CodeInsertAndUpdate.class);
        CodeDelete deleteInfo = JSONUtil.toBean(codeGeneratorConfigEntity.getDeleteInfo(), CodeDelete.class);
        List<CodeQueryField> queryFields = JSONUtil.toList(codeGeneratorConfigEntity.getQueryFields(), CodeQueryField.class);
        List<CodeTableField> tableFields = JSONUtil.toList(codeGeneratorConfigEntity.getTableFields(), CodeTableField.class);
        tableFields.forEach(e -> e.setWidth(e.getWidth() == null ? 0 : e.getWidth()));

        CodeGeneratorConfigForm form = CodeGeneratorConfigForm.builder().basic(basic).fields(fields).insertAndUpdate(insertAndUpdate).deleteInfo(deleteInfo).queryFields(queryFields).tableFields(tableFields).deleteInfo(deleteInfo).build();
        form.setTableName(tableName);
        if (!codeGenerateBaseVariableService.isSupport(form)) {
            return "业务不需要此功能，故没有生成代码；";
        }

        // -------------------- 2 通用模板的变量 --------------------
        Map<String, Object> variablesMap = new HashMap<>();


        Map<String, Object> basicMap = BeanUtil.beanToMap(basic);
        basicMap.put("frontDate", DateUtil.formatLocalDateTime(basic.getFrontDate()));
        basicMap.put("backendDate", DateUtil.formatLocalDateTime(basic.getBackendDate()));

        variablesMap.put("basic", basicMap);
        variablesMap.put("fields", fields);
        variablesMap.put("insertAndUpdate", insertAndUpdate);
        variablesMap.put("deleteInfo", deleteInfo);
        variablesMap.put("queryFields", queryFields);
        variablesMap.put("tableFields", tableFields);
        variablesMap.put("tableName", tableName);

        //名词的大写开头和小写开头

        HashMap<String, String> names = new HashMap<>();
//        names.put("lowerCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, basic.getModuleName()));
        names.put("lowerCamel", StrUtil.lowerFirst(basic.getModuleName()));
//        names.put("upperCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_CAMEL, basic.getModuleName()));
        names.put("upperCamel", StrUtil.upperFirst(basic.getModuleName()));
//        names.put("lowerHyphenCamel", CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, basic.getModuleName()));
        names.put("lowerHyphenCamel",  NamingCase.toKebabCase(basic.getModuleName()));
        variablesMap.put("name", names);

        //主键字段名称和java类型
        CodeField primaryKeycodeField = fields.stream().filter(CodeField::getPrimaryKeyFlag).findFirst().orElse(null);
        if (primaryKeycodeField != null) {
            variablesMap.put("primaryKeyJavaType", primaryKeycodeField.getJavaType());
            variablesMap.put("primaryKeyFieldName", primaryKeycodeField.getFieldName());
            variablesMap.put("primaryKeyColumnName", primaryKeycodeField.getColumnName());
        }

        // -------------------- 3、针对此 模板 的特殊变量 --------------------

        Map<String, Object> specialVariables = codeGenerateBaseVariableService.getInjectVariablesMap(form);
        variablesMap.putAll(specialVariables);

        // -------------------- 4、模板 生成代码 --------------------

        return render("code-generator-template/" + file + ".vm", variablesMap);
    }

    /**
     * 渲染
     *
     * @param templateFile
     * @param variablesMap
     * @return
     */
    private String render(String templateFile, Map<String, Object> variablesMap) {
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true);
        engine.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        engine.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        engine.init();
        Template template = engine.getTemplate(templateFile);

        //加载tools.xml配置文件
        ToolManager toolManager = new ToolManager();
        toolManager.configure("code-generator-template/tools.xml");

        //注入变量
        ToolContext context = toolManager.createContext();
        context.putAll(variablesMap);

        StringWriter sw = new StringWriter();
        template.merge(context, sw);
        return sw.toString();
    }

}

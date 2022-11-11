package com.dqcer.mcdull.auth.provider;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.dqcer.framework.base.utils.StrUtil;

import java.util.*;

/**
 * @author dongqin
 * @description 代码生成器
 * @date  22:21 2021/4/28
 */
@SuppressWarnings({"all"})
public class CodeGeneratorUtil {

    public static final String COM = "com.dqcer.";

    public static final String COM_ = "/com/dqcer/";

    public static final String SRC = "/src/main/java";

    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String API_PROJECT = "com.dqcer.%s.api.%s.%s";


    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help);
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StrUtil.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }


    public static void main(String[] args) {

        /**************************要修改的信息*********************************/
        //  项目的根路径
        final String outputBase = "/mcdull-auth-provider/";
        final String apiPath = "/mcdull-auth-api/";
        //  作者
        String author = "dqcer";
        String dataUrl = "jdbc:mysql://192.168.230.171:3306/mcdull-cloud?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
        String driverName = "com.mysql.cj.jdbc.Driver";
        String username = "root";
        String password = "123456";
        String projectName_ = "mcdull.auth";

        /**********************************end**********************************/


        String projectName = projectName_.replace("-", "");
        String projectNameFolder = projectName_.replace("-", "").replaceAll("\\.","/");

        String modelName = scanner("包名称如：auth、pub");

        String split = scanner("表名");

        // 首字符小写
        String valName = toCamelCase(split);

        //  大写

        String className = captureName(valName);
        //  java代码生成的最终路径
        String outputDir = outputBase + SRC;

        //  各层所对应的最终包路径
        String controller = String.format("%s%s.provider.controller.%s",COM, projectName, modelName);
        String service = String.format("%s%s.provider.service.%s",COM, projectName, modelName);
        String serviceImpl = String.format("%s%s.provider.service.%s.impl",COM, projectName, modelName);
        String dao = String.format("%s%s.provider.dao.mapper.%s",COM, projectName, modelName);
        String repository = String.format("%s%s.provider.dao.repository.%s",COM, projectName, modelName);

        String mapperXmlPath = modelName;

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setDateType(DateType.ONLY_DATE);
        gc.setOutputDir(USER_DIR + outputDir);
        gc.setAuthor(author);
        gc.setOpen(false);
        gc.setSwagger2(false);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dataUrl);
        dsc.setDriverName(driverName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setController(controller);
        pc.setService(service);
        pc.setServiceImpl(serviceImpl);
        pc.setMapper(dao);
        pc.setParent("");
        mpg.setPackageInfo(pc);


        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>(16);
                map.put("dto", String.format("%s%s.dto.%s", COM, projectName, modelName));
                map.put("suffix", "Entity");
                map.put("apiEntity", String.format("com.dqcer.%s.api.%s", projectName, "entity"));
                map.put("apiDto", String.format(API_PROJECT, projectName, "dto", modelName));
                map.put("apiVo", String.format(API_PROJECT, projectName, "vo", modelName));
                map.put("apiConvert", String.format(API_PROJECT, projectName, "convert", modelName));
                map.put("modelName", modelName);
                map.put("baseController", "com.dqcer.common.core.supert.BaseController");
                map.put("result", "com.dqcer.framework.base.wrapper.Result");
                map.put("baseEntity", "com.dqcer.framework.base.entity.BaseEntity");
                map.put("repository",repository);
                map.put("repositoryImpl", repository + ".impl");
                map.put("controller", className + "Controller");
                map.put("serviceName", "I" + className + "Service");
                map.put("serviceImplName",  className + "ServiceImpl");
                map.put("repositoryName",  "I" + className + "Repository");
                map.put("repositoryImplName",   className + "RepositoryImpl");
                map.put("mapperName",   className + "Mapper");
                map.put("convertName",   className + "Convert");
                map.put("dtoName",   className + "LiteDTO");
                map.put("voName",   className + "VO");
                map.put("entityName",   className + "Entity");
                this.setMap(map);
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/template/mapper.xml.ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();

        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return USER_DIR + outputBase + "/src/main/resources/mapper/" + mapperXmlPath
                        + "/" + className + "Mapper" + StringPool.DOT_XML;
            }
        });

        focList.add(new FileOutConfig("/template/entitydto.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return USER_DIR + apiPath + SRC + COM_ + projectNameFolder + "/api"
                        + "/dto/" + modelName + "/" + className + "LiteDTO"
                        + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/template/convert.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return USER_DIR + apiPath + SRC + COM_ + projectNameFolder + "/api"
                        + "/convert/" + modelName + "/" + className + "Convert"
                        + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/template/entity.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return USER_DIR + apiPath + SRC + COM_ + projectNameFolder + "/api"
                        + "/entity/" + "/" + className + "Entity"
                        + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/template/entityvo.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return USER_DIR + apiPath + SRC + COM_ + projectNameFolder + "/api"
                        + "/vo/" + modelName +"/" + className + "VO"
                        + StringPool.DOT_JAVA;
            }
        });


        focList.add(new FileOutConfig("/template/controller.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return USER_DIR + outputBase  + SRC + COM_ + projectNameFolder + "/provider"
                        + "/controller/" + modelName +"/" + className + "Controller"
                        + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/template/service.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return USER_DIR + outputBase  + SRC + COM_ + projectNameFolder + "/provider"
                        + "/service/" + modelName +"/I" + className + "Service"
                        + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/template/serviceImpl.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return USER_DIR + outputBase  + SRC + COM_ + projectNameFolder + "/provider"
                        + "/service/" + modelName +"/impl/" + className + "ServiceImpl"
                        + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/template/repository.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return USER_DIR + outputBase  + SRC + COM_ + projectNameFolder + "/provider"
                        + "/dao/repository/" + modelName +"/I" + className + "Repository"
                        + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/template/repositoryImpl.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return USER_DIR + outputBase  + SRC + COM_ + projectNameFolder + "/provider"
                        + "/dao/repository/" + modelName +"/impl/" + className + "RepositoryImpl"
                        + StringPool.DOT_JAVA;
            }
        });

        focList.add(new FileOutConfig("/template/mapper.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                return USER_DIR + outputBase  + SRC + COM_ + projectNameFolder + "/provider"
                        + "/dao/mapper/" + modelName +"/" + className + "Mapper"
                        + StringPool.DOT_JAVA;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);


        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setController(null);
        templateConfig.setService(null);
        templateConfig.setEntity(null);
        templateConfig.setMapper(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(false);
        strategy.setEntitySerialVersionUID(true);

        strategy.setRestControllerStyle(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        // 写于父类中的公共字段
        String[] strings = { "created_by", "created_time", "updated_by", "updated_time"};
        strategy.setSuperEntityColumns(strings);

        strategy.setInclude(split);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");

        gc.setMapperName("%sMapper");

        ConfigBuilder configBuilder = new ConfigBuilder(pc, dsc, strategy, templateConfig, gc);
        mpg.setConfig(configBuilder);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }


    public static String toCamelCase(String s) {
        s = s.substring(s.indexOf("_")  + 1);
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs= str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

}
package io.gitee.dqcer.mcdull;

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

import java.util.*;

/**
 * 代码生成器
 *
 * @author dqcer
 * @since 2022/12/26 21:12:71
 */
public class CodeGenerator {

    public static final String COM = "io.gitee.";

    public static final String SRC = "/src/main/java";

    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String API_PROJECT = "io.gitee.dqcer.%s.model.%s";
    public static final String WBB_PROJECT = "io.gitee.dqcer.%s.web.%s.%s";

    public static final String BASIC = "io.gitee.dqcer.mcdull.framework.base.";

    public static final String S_ = ".%s";

    public static String MODULE_CODE = "";


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
            if (ipt != null) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }


    /**
     * 运行
     *
     * @param modelName      模块名称 如： auth、pub
     * @param tableName      表名
     * @param isGeneratorWeb 是否生成 controller和service层 和 repository层
     */
    @SuppressWarnings("all")
    public static void run(String modelName, String tableName, boolean isGeneratorWeb) {

        /**************************要修改的信息*********************************/
        //  项目的根路径
        final String outputBase = "/mcdull-bussiness/mcdull-admin/";
        //  作者
        String author = "dqcer";
        String dataUrl = "jdbc:mysql://mcdull.io:3306/mcdull-cloud?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String driverName = "com.mysql.cj.jdbc.Driver";
        String username = "root";
        String password = "123456";
        String projectNames = "mcdull.admin";

        Long parentMenuId = 1L;




        /**********************************end**********************************/
        String outPath = USER_DIR + outputBase + SRC + "/";
        String projectName = projectNames.replace("-", "");
        String split = tableName;

        String modelCode = split.substring(split.indexOf("_")  + 1);
        MODULE_CODE = modelCode.toLowerCase();

        // 首字符小写
        String valName = toCamelCase(split);
        //  大写
        String className = captureName(valName);

        //  各层所对应的最终包路径
        String controllerPackage = String.format(WBB_PROJECT, projectName, "controller", modelName);
        String controllerPath = toPath(controllerPackage);
        String servicePackage = String.format(WBB_PROJECT,projectName, "service", modelName);
        String servicePath = toPath(servicePackage);
        String serviceImplPackage = String.format(WBB_PROJECT,projectName, "service", modelName) + ".impl";
        String serviceImplPath = toPath(serviceImplPackage);
        String repositoryPackage = String.format(WBB_PROJECT,projectName, "dao.repository", modelName);
        String repositoryPath = toPath(repositoryPackage);
        String repositoryImplPackage = String.format(WBB_PROJECT,projectName, "dao.repository", modelName) + ".impl";
        String repositoryImplPath = toPath(repositoryImplPackage);
        String daoPackage = String.format(WBB_PROJECT,projectName, "dao.mapper", modelName);
        String daoPath = toPath(daoPackage);

        String mapperXmlPath = modelName;

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = getGlobalConfig(author, outPath);
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(dataUrl);
        dsc.setDriverName(driverName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setController(controllerPackage);
        pc.setService(servicePackage);
        pc.setServiceImpl(serviceImplPackage);
        pc.setMapper(daoPackage);
        pc.setParent("");
        mpg.setPackageInfo(pc);

        String controllerSuffix = "Controller";
        String serviceSuffix = "Service";
        String serviceImplSuffix = "ServiceImpl";
        String repositorySuffix = "Repository";
        String repositoryImplSuffix = "RepositoryImpl";
        String mapperSuffix = "Mapper";
        String entitySuffix = "DO";
        String voSuffix = "VO";
        String dtoSuffix = "LiteDTO";
        String convertSuffix = "Convert";

        String entityPackage = String.format(API_PROJECT + S_, projectName, "entity", modelName);
        String entityPath = entityPackage.replaceAll("\\.", "/");
        String dtoPackage = String.format(API_PROJECT + S_, projectName, "dto", modelName);
        String dtoPath = dtoPackage.replaceAll("\\.", "/");
        String voPackage = String.format(API_PROJECT + S_, projectName, "vo", modelName);
        String voPath = voPackage.replaceAll("\\.", "/");
        String convertPackage = String.format(API_PROJECT + S_, projectName, "convert", modelName);
        String convertPath = convertPackage.replaceAll("\\.", "/");

        // 自定义配置
        InjectionConfig cfg = getInjectionConfig(projectName, modelName, className, repositoryPackage, repositoryImplPackage, controllerSuffix, serviceSuffix, serviceImplSuffix, repositorySuffix, repositoryImplSuffix, mapperSuffix, entitySuffix, voSuffix, dtoSuffix, convertSuffix, entityPackage, dtoPackage, voPackage, convertPackage);

        // 如果模板引擎是 freemarker
        String templatePath = "/template/mapper.xml.ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义模板
        extracted(outputBase, isGeneratorWeb, outPath, className, controllerPath, servicePath, serviceImplPath, repositoryPath, repositoryImplPath, daoPath, mapperXmlPath, controllerSuffix, serviceSuffix, serviceImplSuffix, repositorySuffix, repositoryImplSuffix, mapperSuffix, entitySuffix, voSuffix, dtoSuffix, convertSuffix, entityPath, dtoPath, voPath, convertPath, templatePath, focList);

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = getTemplateConfig(mpg);
        // 策略配置
        StrategyConfig strategy = getStrategyConfig(split, pc);

        ConfigBuilder configBuilder = new ConfigBuilder(pc, dsc, strategy, templateConfig, gc);
        mpg.setConfig(configBuilder);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    private static GlobalConfig getGlobalConfig(String author, String outPath) {
        GlobalConfig gc = new GlobalConfig();
        gc.setDateType(DateType.ONLY_DATE);
        gc.setOutputDir(outPath);
        gc.setAuthor(author);
        gc.setOpen(false);
        gc.setSwagger2(false);
        gc.setMapperName("%sMapper");
        return gc;
    }

    private static TemplateConfig getTemplateConfig(AutoGenerator mpg) {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setController(null);
        templateConfig.setService(null);
        templateConfig.setEntity(null);
        templateConfig.setMapper(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        return templateConfig;
    }

    private static StrategyConfig getStrategyConfig(String split, PackageConfig pc) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(false);
        strategy.setEntitySerialVersionUID(true);
        strategy.setRestControllerStyle(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        // 写于父类中的公共字段
        String[] strings = { "created_by", "created_time", "updated_by", "updated_time", "del_flag", "del_by"};
        strategy.setSuperEntityColumns(strings);

        strategy.setInclude(split);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        return strategy;
    }

    private static InjectionConfig getInjectionConfig(String projectName, String modelName, String className, String repositoryPackage, String repositoryImplPackage, String controllerSuffix, String serviceSuffix, String serviceImplSuffix, String repositorySuffix, String repositoryImplSuffix, String mapperSuffix, String entitySuffix, String voSuffix, String dtoSuffix, String convertSuffix, String entityPackage, String dtoPackage, String voPackage, String convertPackage) {
        // 自定义配置
        return new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>(16);
                map.put("moduleCode", MODULE_CODE);
                map.put("dto", String.format("%s%s.dto.%s", COM, projectName, modelName));
                map.put("suffix", entitySuffix);
                map.put("apiEntity", entityPackage);
                map.put("apiDto", dtoPackage);
                map.put("apiVo", voPackage);
                map.put("apiConvert", convertPackage);
                map.put("modelName", modelName);
//                map.put("baseController", "io.gitee.common.core.support.BaseController");
                map.put("result", BASIC + "wrapper.Result");
                map.put("baseEntity", BASIC + "entity.BaseDO");
                map.put("baseVO", BASIC + "vo.VO");
                map.put("GlobalConstant", BASIC + "constants.GlobalConstant");
                map.put("DelFlayEnum", BASIC + "enums.DelFlayEnum");
                map.put("DatabaseRowException", BASIC + "exception.DatabaseRowException");
                map.put("ObjUtil", BASIC + "util.ObjUtil");
                map.put("PagedVO", BASIC + "vo.PagedVO");
                map.put("StatusDTO", BASIC + "dto.StatusDTO");
                map.put("IdDTO", BASIC + "dto.IdDTO");
                map.put("UserContextHolder", BASIC + "storage.UserContextHolder");
                map.put("CodeEnum", BASIC + "wrapper.CodeEnum");
                map.put("PageUtil", BASIC + "util.PageUtil");
                map.put("Authorized", BASIC + "annotation.Authorized");
                map.put("ValidGroup", BASIC + "validator.ValidGroup");
                map.put("PkDTO", BASIC + "dto.PkDTO");
                map.put("repository",repositoryPackage);
                map.put("repositoryImpl", repositoryImplPackage);
                map.put("controller", className + controllerSuffix);
                map.put("serviceName", "I" + className + serviceSuffix);
                map.put("serviceImplName",  className + serviceImplSuffix);
                map.put("repositoryName",  "I" + className + repositorySuffix);
                map.put("repositoryImplName",   className + repositoryImplSuffix);
                map.put("mapperName",   className + mapperSuffix);
                map.put("convertName",   className + convertSuffix);
                map.put("dtoName",   className + dtoSuffix);
                map.put("voName",   className + voSuffix);
                map.put("entityName",   className + entitySuffix);
                this.setMap(map);
            }
        };
    }

    private static void extracted(String outputBase, boolean isGeneratorWeb, String outPath, String className, String controllerPath, String servicePath, String serviceImplPath, String repositoryPath, String repositoryImplPath, String daoPath, String mapperXmlPath, String controllerSuffix, String serviceSuffix, String serviceImplSuffix, String repositorySuffix, String repositoryImplSuffix, String mapperSuffix, String entitySuffix, String voSuffix, String dtoSuffix, String convertSuffix, String entityPath, String dtoPath, String voPath, String convertPath, String templatePath, List<FileOutConfig> focList) {
        focList.add(new FileOutConfig("/template/entity.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return outPath + entityPath + "/" + className + entitySuffix + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig("/template/mapper.java.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return outPath + daoPath +"/" + className + mapperSuffix + StringPool.DOT_JAVA;
            }
        });
        if (isGeneratorWeb) {
            focList.add(new FileOutConfig(templatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                    return USER_DIR + outputBase + "/src/main/resources/mapper/" + mapperXmlPath
                            + "/" + className + mapperSuffix + StringPool.DOT_XML;
                }
            });

            focList.add(new FileOutConfig("/template/entitydto.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return outPath + dtoPath + "/" + className + dtoSuffix + StringPool.DOT_JAVA;
                }
            });
            focList.add(new FileOutConfig("/template/convert.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return outPath + convertPath + "/" + className + convertSuffix + StringPool.DOT_JAVA;
                }
            });
            focList.add(new FileOutConfig("/template/entityvo.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return outPath + voPath +"/" + className + voSuffix + StringPool.DOT_JAVA;
                }
            });
            focList.add(new FileOutConfig("/template/controller.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return outPath + controllerPath +"/" + className + controllerSuffix + StringPool.DOT_JAVA;
                }
            });
            focList.add(new FileOutConfig("/template/service.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return outPath + servicePath + "/I" + className + serviceSuffix + StringPool.DOT_JAVA;
                }
            });
            focList.add(new FileOutConfig("/template/serviceImpl.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return outPath + serviceImplPath +"/" + className + serviceImplSuffix + StringPool.DOT_JAVA;
                }
            });
            focList.add(new FileOutConfig("/template/repository.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return outPath + repositoryPath + "/I" + className + repositorySuffix + StringPool.DOT_JAVA;
                }
            });
            focList.add(new FileOutConfig("/template/repositoryImpl.java.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return outPath + repositoryImplPath +"/" + className + repositoryImplSuffix + StringPool.DOT_JAVA;
                }
            });
        }

    }

    private static String toPath(String packageName) {
        return packageName.replaceAll("\\.", "/");
    }


    public static String toCamelCase(String s) {
        s = s.substring(s.indexOf('_')  + 1);
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

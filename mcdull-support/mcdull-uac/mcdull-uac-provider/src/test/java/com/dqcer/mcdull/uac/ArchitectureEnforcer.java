package com.dqcer.mcdull.uac;

import com.dqcer.framework.base.dto.DTO;
import com.dqcer.framework.base.entity.DO;
import com.dqcer.framework.base.vo.VO;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;

import java.util.LinkedList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * 架构守护神规则库,提供了静态检查机制(配合单元测试使用)，杜绝线上出现不合规范的使用
 *
 * @author dqcer
 * @date 2022/10/04
 */
public final class ArchitectureEnforcer {

    public static final List<ArchRule> requiredRules = new LinkedList<>();

    public ArchitectureEnforcer() {
    }

    static {
        requiredRules.add(mapperNamingRules());

        requiredRules.add(dtoNamingRules());
        requiredRules.add(doNamingRules());
        requiredRules.add(voNamingRules());
//        requiredRules.add(interfacesNamingRules());
        requiredRules.add(enumNamingRules());

        // 禁止使用e.printStackTrace, System.err/System.out
        requiredRules.add(GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.as("禁止使用 e.printStackTrace, System.err/System.out"));

        // 不能直接抛出 Throwable、Exception、RuntimeException异常
        requiredRules.add(GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.as("不能直接抛出 Throwable、Exception、RuntimeException异常"));
    }

//    /**
//     * service 层命名规则
//     *
//     * @return {@link ArchRule}
//     */
//    public static ArchRule serviceNamingRules() {
//        return classes()
//                .that()
//                .resideInAPackage("..service")
//                .should()
//                .haveSimpleNameEndingWith("Service")
//                .allowEmptyShould(true)
//                .as("service 层下的类应该以'Service'结尾");
//    }
//
//    /**
//     * controller 层命名规则
//     *
//     * @return {@link ArchRule}
//     */
//    public static ArchRule controllerNamingRules() {
//        return classes()
//                .that()
//                .resideInAPackage("..controller")
//                .should()
//                .haveSimpleNameEndingWith("Controller")
//                .allowEmptyShould(true)
//                .as("controller 层下的类应该以'Controller'结尾");
//    }

    /**
     * mapper 层命名规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule mapperNamingRules() {
        return classes()
                .that()
                .resideInAPackage("..dao.mapper..")
                .should()
                .haveSimpleNameEndingWith("Mapper")
                .allowEmptyShould(true)
                .as("mapper 层下的类应该以'Mapper'结尾");
    }

    /**
     * DO 命名规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule doNamingRules() {
        return classes().that().implement(DO.class)
                .should().haveSimpleNameEndingWith("DO").allowEmptyShould(true)
                .as("实现DO的类名应该以'DO'结尾");
    }

    /**
     * dto 命名规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule dtoNamingRules() {
        return classes().that().implement(DTO.class)
                .should().haveSimpleNameEndingWith("DTO").allowEmptyShould(true)
                .as("实现DTO的类名应该以'DTO'结尾");
    }

    /**
     * vo 层命名规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule voNamingRules() {
        return classes().that().implement(VO.class)
                .should().haveSimpleNameEndingWith("VO").allowEmptyShould(true)
                .as("实现VO的类名应该以'VO'结尾");
    }


    /**
     * 接口命名规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule interfacesNamingRules() {
        return classes().that().areInterfaces().should().haveSimpleNameStartingWith("I").as("接口名称必须以I开头");
    }

    /**
     * enum命名规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule enumNamingRules() {
        return classes().that().areEnums().should().haveSimpleNameEndingWith("Enum").as("枚举类应该以'Enum'结尾");
    }



}

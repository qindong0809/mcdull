package com.dqcer.mcdull.framework.enforcer;

import com.dqcer.mcdull.framework.base.dto.DTO;
import com.dqcer.mcdull.framework.base.entity.DO;
import com.dqcer.mcdull.framework.base.vo.VO;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;

import java.util.LinkedList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * 架构守护神规则库,提供了静态检查机制(配合单元测试使用)，杜绝线上出现不合规范的使用
 *
 * @author dqcer
 * @date 2022/10/04
 */
public final class ArchitectureEnforcer {

    public static final List<ArchRule> REQUIRED_RULES = new LinkedList<>();

    public ArchitectureEnforcer() {
    }

    static {

        // 层级调用关系检查
        REQUIRED_RULES.add(layerChecks());

        REQUIRED_RULES.add(mapperNamingRules());
        REQUIRED_RULES.add(dtoNamingRules());
        REQUIRED_RULES.add(doNamingRules());
        REQUIRED_RULES.add(voNamingRules());
        REQUIRED_RULES.add(enumNamingRules());

        // 禁止使用e.printStackTrace, System.err/System.out
        REQUIRED_RULES.add(GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.as("禁止使用 e.printStackTrace, System.err/System.out"));

        // 不能直接抛出 Throwable、Exception、RuntimeException异常
        REQUIRED_RULES.add(GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.as("不能直接抛出 Throwable、Exception、RuntimeException异常"));
    }


    /**
     * 层级调用规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule layerChecks() {
        return layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..web.controller..")
                .layer("ServerFeign").definedBy("..web.feign..")
                .layer("Service").definedBy("..web.service..")
                .layer("Manager").definedBy("..web.manager..")
                .layer("Repository").definedBy("..web.dao.repository..")
                .layer("Mapper").definedBy("..web.dao.mapper..")

                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "ServerFeign").as("Service业务层仅被Controller层和ServerFeign层调用")
                .whereLayer("Manager").mayOnlyBeAccessedByLayers("Service").as("Manager数据库访问层仅被Service业务层调用")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service", "Manager").as("Repository数据库包装层仅被Service业务层、Manager通用逻辑层调用")
                .whereLayer("Mapper").mayOnlyBeAccessedByLayers("Repository").as("Mapper数据库访问层仅被Repository数据库包装层调用");
    }

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
     * enum命名规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule enumNamingRules() {
        return classes().that().areEnums().should().haveSimpleNameEndingWith("Enum").as("枚举类应该以'Enum'结尾");
    }



}

package io.gitee.dqcer.mcdull.demo;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import io.gitee.dqcer.mcdull.framework.enforcer.ArchitectureEnforcer;

/**
 * demo 模块架构守护测试。
 *
 * <p>不排除 jar：demo-facade 以 jar 形式进入测试 classpath。
 * jar 是否真正加载由 {@link #FACADE_CLASSES_LOADED} 自动校验。
 * 架构规则采用零容忍策略，任何违规都会导致构建失败。
 */
@AnalyzeClasses(
        packages = "io.gitee.dqcer.mcdull.demo",
        importOptions = ImportOption.DoNotIncludeTests.class)
class DemoArchitectureTest {

    @ArchTest
    static final ArchRule FACADE_CLASSES_LOADED = ArchitectureEnforcer.facadeClassesLoadedRule();

    @ArchTest
    static final ArchRule LAYER_CALL_RULES = ArchitectureEnforcer.layerChecks();

    @ArchTest
    static final ArchRule MAPPER_NAMING_RULES = ArchitectureEnforcer.mapperNamingRules();

    @ArchTest
    static final ArchRule DTO_NAMING_RULES = ArchitectureEnforcer.dtoNamingRules();

    @ArchTest
    static final ArchRule ENTITY_NAMING_RULES = ArchitectureEnforcer.entityNamingRules();

    @ArchTest
    static final ArchRule VO_NAMING_RULES = ArchitectureEnforcer.voNamingRules();

    @ArchTest
    static final ArchRule BO_NAMING_RULES = ArchitectureEnforcer.boNamingRules();

    @ArchTest
    static final ArchRule ENUM_NAMING_RULES = ArchitectureEnforcer.enumNamingRules();

    @ArchTest
    static final ArchRule DTO_PLACEMENT_RULES = ArchitectureEnforcer.dtoPlacementRules();

    @ArchTest
    static final ArchRule VO_PLACEMENT_RULES = ArchitectureEnforcer.voPlacementRules();

    @ArchTest
    static final ArchRule ENTITY_PLACEMENT_RULES = ArchitectureEnforcer.entityPlacementRules();

    @ArchTest
    static final ArchRule FACADE_MUST_NOT_DEPEND_ON_PROVIDER = ArchitectureEnforcer.facadeDependencyRules();

    @ArchTest
    static final ArchRule NO_STANDARD_STREAMS = ArchitectureEnforcer.noStandardStreamsRule();

    @ArchTest
    static final ArchRule NO_GENERIC_EXCEPTIONS = ArchitectureEnforcer.noGenericExceptionsRule();
}

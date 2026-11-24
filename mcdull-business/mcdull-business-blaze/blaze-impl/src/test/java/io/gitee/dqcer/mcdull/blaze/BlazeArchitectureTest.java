package io.gitee.dqcer.mcdull.blaze;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import io.gitee.dqcer.mcdull.framework.enforcer.ArchitectureEnforcer;

/**
 * blaze 模块架构守护测试。
 *
 * <p>blaze-facade 是空模块，分析范围内没有 {@code ..facade..} 契约类，
 * 因此不声明 {@code facadeDependencyRules} —— 该规则在这种情况下 0 命中静默通过，属于假守护。
 * 架构规则采用零容忍策略，任何违规都会导致构建失败。
 */
@AnalyzeClasses(
        packages = "io.gitee.dqcer.mcdull.blaze",
        importOptions = ImportOption.DoNotIncludeTests.class)
class BlazeArchitectureTest {

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
    static final ArchRule NO_STANDARD_STREAMS = ArchitectureEnforcer.noStandardStreamsRule();

    @ArchTest
    static final ArchRule NO_GENERIC_EXCEPTIONS = ArchitectureEnforcer.noGenericExceptionsRule();
}

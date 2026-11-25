package io.gitee.dqcer.mcdull.framework.enforcer;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;
import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.gitee.dqcer.mcdull.framework.base.support.Entity;
import io.gitee.dqcer.mcdull.framework.base.support.VO;

import java.util.LinkedList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * 架构守护神规则库,提供了静态检查机制(配合单元测试使用)，杜绝线上出现不合规范的使用
 *
 * @author dqcer
 * @since 2022/10/04
 */
public final class ArchitectureEnforcer {

    public static final List<ArchRule> REQUIRED_RULES = new LinkedList<>();

    public ArchitectureEnforcer() {
    }

    static {

        // 层级调用关系检查
        REQUIRED_RULES.add(layerChecks());

        // 模型边界：Entity 不得出现在 Controller 公共方法签名
        REQUIRED_RULES.add(entityMustNotLeakToControllerRule());

        // 命名按包判定，角色归属按标记接口判定，两者不可混用
        REQUIRED_RULES.add(mapperNamingRules());
        REQUIRED_RULES.add(dtoNamingRules());
        REQUIRED_RULES.add(entityNamingRules());
        REQUIRED_RULES.add(voNamingRules());
        REQUIRED_RULES.add(boNamingRules());
        REQUIRED_RULES.add(enumNamingRules());

        REQUIRED_RULES.add(dtoPlacementRules());
        REQUIRED_RULES.add(voPlacementRules());
        REQUIRED_RULES.add(entityPlacementRules());
        // 不给 BO 设位置规则：implement() 取传递闭包，LogonVO extends UserSessionBO 这种
        // 「视图对象复用会话快照字段」是合法写法，BO 位置规则会把它误报成位置错误

        // facade 契约包不得依赖 provider 实现包（防止 facade/impl 拆分包复发）
        REQUIRED_RULES.add(facadeDependencyRules());

        // 禁止使用e.printStackTrace, System.err/System.out
        REQUIRED_RULES.add(noStandardStreamsRule());

        // 不能直接抛出 Throwable、Exception、RuntimeException异常
        REQUIRED_RULES.add(noGenericExceptionsRule());
    }

    /**
     * 冻结规则基线：首次运行记录当前违规，后续仅拦截新增违规。
     * <p>
     * 默认架构守护采用零容忍策略，业务测试类直接声明规则，不使用本方法。
     * 仅当某模块存在无法立即整改的存量违规、需要渐进治理时，才用它包装规则。
     * <p>
     * 使用时需在测试 classpath 提供 {@code archunit.properties} 并开启
     * {@code freeze.store.default.allowStoreCreation=true}，且将生成的 {@code archunit_store/}
     * 基线文件随代码提交，否则 CI 每次都相当于首次运行，新增违规会被自动冻结。
     * 基线只是过渡手段，目标仍是清零后回到零容忍。
     *
     * @param rule 原始规则
     * @return 冻结后的规则
     */
    public static ArchRule frozen(ArchRule rule) {
        return FreezingArchRule.freeze(rule);
    }

    /**
     * facade 契约类必须已进入测试 classpath。
     * <p>
     * {@code @AnalyzeClasses} 若误加 {@code DoNotIncludeJars}，{@code ..facade..} 内将无类可查，
     * {@link #facadeDependencyRules()} 会 0 命中静默通过。本规则用 {@code allowEmptyShould(false)}
     * 将"0 命中"变为失败，把原先靠注释维持的约束变成可执行检查。
     * <p>
     * 仅适用于 facade 中确有契约类的模块（system / workflow / demo）。
     * blaze 的 facade 为空模块、focus-facade 仅有一个不在 facade 包的 Main，
     * 这两个模块的 facadeDependencyRules 无实际检查对象，不应声明本规则。
     *
     * @return 类加载校验规则
     */
    public static ArchRule facadeClassesLoadedRule() {
        return classes()
                .that().resideInAPackage("..facade..")
                .should().haveNameMatching(".*")
                .allowEmptyShould(false)
                .as("facade 契约类必须已进入测试 classpath（禁止排除 jar）");
    }


    /**
     * 层级调用规则
     * <p>
     * 注意：{@code LayeredArchitecture.as(..)} 会覆盖整条复合规则的描述，只有最后一次生效，
     * 因此此处只在链尾调用一次，不要把各层的说明写成多个 {@code as(..)}。
     *
     * @return {@link ArchRule}
     */
    public static ArchRule layerChecks() {
        return layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..web.controller..")
                .optionalLayer("Entry").definedBy("..web.feign..", "..web.def..", "..flow..", "..config..")
                .layer("Service").definedBy("..web.service..", "..provider.application..")
                .optionalLayer("Manager").definedBy("..web.manager..")
                .layer("Mapper").definedBy("..web.mapper..")

                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Entry", "Manager")
                .whereLayer("Manager").mayOnlyBeAccessedByLayers("Service", "Entry")
                .whereLayer("Mapper").mayOnlyBeAccessedByLayers("Service", "Manager")
                .as("分层调用约束：Controller 不得被任何层访问；Service 可被 Controller/Entry/Manager 调用；"
                        + "Manager 可被 Service/Entry 调用；仅 Service/Manager 可调用 Mapper")
                .because("Entry 是对外/对框架的入口与基础设施适配（Feign 服务端、facade SPI、流程编排、监听器、"
                        + "定时任务、序列化器与常量），其中序列化器和常量会被 model 反向引用，因此 Entry 本身不作为被访问方约束；"
                        + "MyBatis-Plus 的 ServiceImpl<M,T> 使 Service 必然直接依赖 Mapper，"
                        + "Repository 包装层只存在于代码生成模板，手写链路不经过它");
    }

    /**
     * 模型边界规则：Controller 公共方法不得以 Entity（或实现 Entity 标记接口的类型）作为返回类型。
     * <p>
     * 防止表结构与内部字段（如 loginPwd、usedPassword）随响应外泄；Controller 应返回 VO / DTO。
     * <p>
     * 已知边界：本规则检查方法的原始返回类型，因此能拦截 {@code UserEntity get()}，
     * 但不能识别 {@code Result<UserEntity>} 之类泛型参数中的 Entity——该场景由 code review 与
     * {@code doc/架构分层与依赖治理规范.md} 第七节共同覆盖，避免为单一规则引入复杂的泛型递归解析。
     *
     * @return {@link ArchRule}
     */
    public static ArchRule entityMustNotLeakToControllerRule() {
        return noClasses()
                .that()
                .resideInAPackage("..web.controller..")
                .should(new ArchCondition<JavaClass>("not return Entity from public methods") {
                    @Override
                    public void check(JavaClass javaClass, ConditionEvents events) {
                        for (JavaMethod method : javaClass.getMethods()) {
                            if (!method.getModifiers().contains(JavaModifier.PUBLIC)) {
                                continue;
                            }
                            JavaClass returnType = method.getRawReturnType();
                            boolean isEntity = returnType.getName().equals(Entity.class.getName())
                                    || returnType.getInterfaces().stream()
                                            .anyMatch(i -> i.getName().equals(Entity.class.getName()));
                            if (isEntity) {
                                events.add(SimpleConditionEvent.violated(method,
                                        method.getDescription() + " returns Entity " + returnType.getName()));
                            }
                        }
                    }
                })
                .allowEmptyShould(true)
                .as("Controller 公共方法不得返回 Entity（含实现 Entity 标记接口的类型）")
                .because("Entity 携带表结构与敏感字段，必须转换为 VO/DTO 后才能出 Controller 边界");
    }

    /**
     * mapper 层命名规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule mapperNamingRules() {
        return classes()
                .that()
                .resideInAPackage("..mapper..")
                .should()
                .haveSimpleNameEndingWith("Mapper")
                .allowEmptyShould(true)
                .as("mapper 层下的类应该以'Mapper'结尾");
    }

    /**
     * 持久化对象命名规则：按包判定，不看标记接口
     *
     * @return {@link ArchRule}
     */
    public static ArchRule entityNamingRules() {
        return roleNamingRule("..entity..", "Entity");
    }

    /**
     * dto 命名规则：按包判定，不看标记接口
     *
     * @return {@link ArchRule}
     */
    public static ArchRule dtoNamingRules() {
        return roleNamingRule("..dto..", "DTO");
    }

    /**
     * vo 命名规则：按包判定，不看标记接口
     *
     * @return {@link ArchRule}
     */
    public static ArchRule voNamingRules() {
        return classes()
                .that().resideInAPackage("..vo..")
                .and().resideOutsideOfPackage("..facade..")
                .and().areNotNestedClasses()
                .and().doNotHaveSimpleName("package-info")
                .should().haveSimpleNameEndingWith("VO").allowEmptyShould(true)
                .as("..vo.. 包下的类名应该以'VO'结尾（facade 契约层除外："
                        + "契约结果对象按业务语义命名，如 StartProcessResult）");
    }

    /**
     * bo 命名规则：按包判定，不看标记接口
     *
     * @return {@link ArchRule}
     */
    public static ArchRule boNamingRules() {
        return roleNamingRule("..bo..", "BO");
    }

    /**
     * 按包判定的角色命名规则。
     * <p>
     * 必须排除 {@code package-info} 与 Lombok {@code @Builder} 生成的嵌套 builder：
     * 前者是包级元数据类，后者的名字由外层类名拼 {@code Builder} 决定，两者都不受业务命名约定支配。
     * 这里用 {@code areNotNestedClasses} 而不是 {@code areNotInnerClasses}：
     * Lombok 生成的是 static nested class，不属于 Java 语义的 inner class，后者一条也排除不掉。
     *
     * @param packagePattern 角色包
     * @param suffix         要求的类名后缀
     * @return {@link ArchRule}
     */
    private static ArchRule roleNamingRule(String packagePattern, String suffix) {
        return classes()
                .that().resideInAPackage(packagePattern)
                .and().areNotNestedClasses()
                .and().doNotHaveSimpleName("package-info")
                .should().haveSimpleNameEndingWith(suffix).allowEmptyShould(true)
                .as(String.format("%s 包下的类名应该以'%s'结尾", packagePattern, suffix));
    }

    /**
     * 持久化对象位置规则：标记接口只用来判定角色归属，不用来判定命名。
     * <p>
     * {@code implement(..)} 取的是传递闭包，所以 {@code VO extends XxxEntity} 会被抓成
     * 「视图对象不该是持久化对象」——这是真实缺陷；而把同一条判断写成命名规则，
     * 报出来的却是「你名字没起对」，方向错，还会连累 {@code XxxQueryDTO extends PagedDTO}
     * 这类完全合法的继承。
     *
     * @return {@link ArchRule}
     */
    public static ArchRule entityPlacementRules() {
        return classes().that().implement(Entity.class)
                .should().resideInAPackage("..entity..").allowEmptyShould(true)
                .as("实现 Entity 标记接口的类应位于 ..entity.. 包下");
    }

    /**
     * dto 位置规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule dtoPlacementRules() {
        return classes().that().implement(DTO.class)
                .should().resideInAPackage("..dto..").allowEmptyShould(true)
                .as("实现 DTO 标记接口的类应位于 ..dto.. 包下");
    }

    /**
     * vo 位置规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule voPlacementRules() {
        return classes().that().implement(VO.class)
                .and().resideOutsideOfPackage("..facade..")
                .should().resideInAPackage("..vo..").allowEmptyShould(true)
                .as("实现 VO 标记接口的类应位于 ..vo.. 包下（facade 契约层除外："
                        + "facade 中的传输对象作为 VO 基类被 UserVO extends UserDTO 复用，位置由契约层约定）");
    }


    /**
     * enum命名规则
     *
     * @return {@link ArchRule}
     */
    public static ArchRule enumNamingRules() {
        return classes().that().areEnums().should().haveSimpleNameEndingWith("Enum").as("枚举类应该以'Enum'结尾");
    }

    /**
     * facade 依赖规则：对外契约包(facade)只允许被实现包(provider)依赖，不得反向依赖，
     * 且 facade 与 impl 必须使用不同包名，杜绝拆分包(Split Package)
     *
     * @return {@link ArchRule}
     */
    public static ArchRule facadeDependencyRules() {
        return noClasses()
                .that().resideInAPackage("..facade..")
                .should().dependOnClassesThat().resideInAPackage("..provider..")
                .allowEmptyShould(true)
                .as("facade 契约包不得依赖 impl 实现包（防止 Split Package 复发）");
    }

    /**
     * 禁止访问标准输出流
     *
     * @return {@link ArchRule}
     */
    public static ArchRule noStandardStreamsRule() {
        return GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS
                .as("禁止使用 e.printStackTrace, System.err/System.out");
    }

    /**
     * 禁止抛出泛型异常
     *
     * @return {@link ArchRule}
     */
    public static ArchRule noGenericExceptionsRule() {
        return GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS
                .as("不能直接抛出 Throwable、Exception、RuntimeException异常");
    }



}

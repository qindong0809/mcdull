package io.gitee.dqcer.mcdull.system;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;
import io.gitee.dqcer.mcdull.framework.enforcer.ArchitectureEnforcer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 架构诊断报告：一次跑完全部规则，输出合并后的违规清单，不产生断言。
 * <p>
 * 与 {@link SystemArchitectureTest} 的分工：那个类是门禁，一条规则一个测试项、红了就阻塞；
 * 本类是调参用的望远镜，改规则时用它一次看清全部影响面，永远不让构建变红。
 * <p>
 * 必须带 {@link ImportOption.DoNotIncludeTests} 导入：否则测试类自身会进入分析范围，
 * 被「禁止标准输出」规则判为违规。
 */
@Tag("diagnostic")
class ArchitectureDiagnosticTest {

    private static final String PACKAGE_ROOT = "io.gitee.dqcer.mcdull.system";

    /**
     * 违规明细行里的第一个 &lt;...&gt; 是依赖发起方，用它做聚合口径。
     * 用非贪婪的「到下一个 &gt; 为止」而不是 [\w.$]+，否则带方法参数的行（如 getMap(java.lang.String)）
     * 会匹配失败并落到第二个尖括号上，把被依赖方当成发起方。
     */
    private static final Pattern ORIGIN = Pattern.compile("<([^>]+)>");

    private static final Map<String, ArchRule> RULES = new LinkedHashMap<>();

    static {
        RULES.put("分层调用约束", ArchitectureEnforcer.layerChecks());
        RULES.put("Mapper 命名", ArchitectureEnforcer.mapperNamingRules());
        RULES.put("DTO 命名", ArchitectureEnforcer.dtoNamingRules());
        RULES.put("Entity 命名", ArchitectureEnforcer.entityNamingRules());
        RULES.put("VO 命名", ArchitectureEnforcer.voNamingRules());
        RULES.put("BO 命名", ArchitectureEnforcer.boNamingRules());
        RULES.put("枚举命名", ArchitectureEnforcer.enumNamingRules());
        RULES.put("DTO 位置", ArchitectureEnforcer.dtoPlacementRules());
        RULES.put("VO 位置", ArchitectureEnforcer.voPlacementRules());
        RULES.put("Entity 位置", ArchitectureEnforcer.entityPlacementRules());
        RULES.put("facade 不依赖 provider", ArchitectureEnforcer.facadeDependencyRules());
        RULES.put("禁止标准输出", ArchitectureEnforcer.noStandardStreamsRule());
        RULES.put("禁止泛型异常", ArchitectureEnforcer.noGenericExceptionsRule());
    }

    @Test
    @DisplayName("输出全部架构规则的合并违规报告")
    void reportAllRules() {
        long start = System.currentTimeMillis();
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages(PACKAGE_ROOT);
        long elapsed = System.currentTimeMillis() - start;

        StringBuilder out = new StringBuilder(64 * 1024);
        out.append("\n================ 架构诊断报告 ================\n");
        out.append("分析包根 : ").append(PACKAGE_ROOT).append('\n');
        out.append("导入类数 : ").append(classes.size()).append("  (耗时 ").append(elapsed).append(" ms)\n\n");

        List<String> failed = new ArrayList<>();
        Map<String, List<String>> violations = new LinkedHashMap<>();
        int total = 0;

        out.append(String.format("%-6s %6s  %s%n", "状态", "条数", "规则"));
        for (Map.Entry<String, ArchRule> entry : RULES.entrySet()) {
            // 每条规则只求值一次：layerChecks 单次就要 3 秒以上，明细阶段重跑会整体翻倍
            EvaluationResult result = entry.getValue().evaluate(classes);
            List<String> details = result.hasViolation() ? result.getFailureReport().getDetails()
                    : List.of();
            total += details.size();
            if (!details.isEmpty()) {
                failed.add(entry.getKey());
                violations.put(entry.getKey(), details);
            }
            out.append(String.format("%-6s %6d  %s%n", details.isEmpty() ? "PASS" : "FAIL",
                    details.size(), entry.getKey()));
        }

        out.append("\n合计违规 ").append(total).append(" 条，涉及 ").append(failed.size()).append(" 条规则\n");

        int maxDetails = detailLimit();
        for (String name : failed) {
            List<String> details = violations.get(name);
            out.append("\n---------------- ").append(name).append(" (").append(details.size()).append(" 条) ----------------");
            out.append(groupByOrigin(details, maxDetails));
            int hidden = Math.max(0, details.size() - maxDetails);
            if (hidden > 0) {
                out.append("\n  ... 另有 ").append(hidden)
                        .append(" 条未打印（-Darch.diag.maxDetails=N 调整）");
            }
            out.append('\n');
        }
        out.append("=============================================\n");

        System.out.println(out);
    }

    /**
     * 按发起方包聚合，便于判断一条规则的影响面集中在哪一层。
     */
    private String groupByOrigin(List<String> details, int maxDetails) {
        Map<String, Integer> buckets = new LinkedHashMap<>();
        for (String detail : details) {
            Matcher matcher = ORIGIN.matcher(detail);
            String key;
            if (matcher.find()) {
                key = shorten(matcher.group(1));
            } else if (detail.startsWith("Layer")) {
                key = "（层定义为空）";
            } else {
                key = "（其他）";
            }
            buckets.merge(key, 1, Integer::sum);
        }

        StringBuilder sb = new StringBuilder(4 * 1024);
        sb.append("\n  影响面: ");
        buckets.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(8)
                .forEach(e -> sb.append(e.getKey()).append('=').append(e.getValue()).append("  "));

        sb.append("\n  明细:");
        details.stream().limit(maxDetails).forEach(d -> sb.append("\n    ").append(d));
        return sb.toString();
    }

    private String shorten(String fullyQualifiedName) {
        List<String> parts = Arrays.asList(fullyQualifiedName.split("\\."));
        int from = parts.indexOf("provider");
        if (from < 0) {
            return fullyQualifiedName;
        }
        return String.join(".", parts.subList(from, Math.min(from + 3, parts.size())));
    }

    private int detailLimit() {
        String configured = System.getProperty("arch.diag.maxDetails");
        if (configured == null) {
            return 30;
        }
        try {
            return Math.max(1, Integer.parseInt(configured.trim()));
        } catch (NumberFormatException ignored) {
            return 30;
        }
    }
}

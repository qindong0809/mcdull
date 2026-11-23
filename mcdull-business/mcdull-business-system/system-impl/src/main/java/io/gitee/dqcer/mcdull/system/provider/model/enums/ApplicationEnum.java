package io.gitee.dqcer.mcdull.system.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import lombok.Getter;

/**
 * 应用分类枚举
 *
 * @author dqcer
 * @since 2026/11/23
 */
@Getter
public enum ApplicationEnum implements IEnum<String> {

    /**
     * 系统管理
     */
    SYSTEM("system", "系统管理", "system:", "/system/"),

    /**
     * 工作流中心
     */
    WORKFLOW("workflow", "工作流中心", "workflow:", "/workflow/"),

    /**
     * AI 助手
     */
    AI("ai", "AI 助手", "ai:", "/ai/"),

    /**
     * Agent 治理
     */
    AGENT_GOV("gov", "Agent 治理", "agent:", "/agent/");

    /**
     * 权限码前缀，用户拥有该前缀下的任意权限即可看到此应用
     */
    private final String permissionPrefix;

    /**
     * 菜单路径片段，用于兜底识别无权限码的页面（如工作流概览）
     */
    private final String pathPrefix;

    ApplicationEnum(String code, String text, String permissionPrefix, String pathPrefix) {
        init(code, text);
        this.permissionPrefix = permissionPrefix;
        this.pathPrefix = pathPrefix;
    }
}

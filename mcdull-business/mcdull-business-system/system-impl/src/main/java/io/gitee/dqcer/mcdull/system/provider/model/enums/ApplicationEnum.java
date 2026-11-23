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
    SYSTEM("system", "系统管理", "system:"),

    /**
     * 工作流中心
     */
    WORKFLOW("workflow", "工作流中心", "workflow:"),

    /**
     * AI 助手
     */
    AI("ai", "AI 助手", "ai:");

    /**
     * 权限码前缀，用户拥有该前缀下的任意权限即可看到此应用
     */
    private final String permissionPrefix;

    ApplicationEnum(String code, String text, String permissionPrefix) {
        init(code, text);
        this.permissionPrefix = permissionPrefix;
    }
}

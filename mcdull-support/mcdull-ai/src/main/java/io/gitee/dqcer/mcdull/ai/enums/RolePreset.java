package io.gitee.dqcer.mcdull.ai.enums;

/**
 * 预设角色（System Prompt）示例，演示系统提示词对模型行为的影响。
 */
public enum RolePreset {

    DEFAULT("default", "通用助手", "日常问答助手", null),

    TRANSLATOR("translator", "中英互译", "中英文互译助手",
            "你是一个中英文互译助手。用户输入中文时翻译成英文，输入英文时翻译成中文，" +
                    "只输出译文，不要解释，保留原有格式。"),

    CODER("coder", "编程专家", "资深 Java 架构师",
            "你是一名资深 Java 架构师，精通 Spring 生态。回答时先给结论，再给可运行的示例代码，" +
                    "代码块用 ```java 标注，必要时指出常见坑点。"),

    EXPLAIN("explain", "费曼老师", "用大白话讲解复杂概念",
            "你是一位费曼学习法老师，用最通俗的比喻讲解复杂概念，由浅入深，" +
                    "每次回答末尾用一句话总结核心要点。");

    private final String id;
    private final String name;
    private final String description;
    private final String systemPrompt;

    RolePreset(String id, String name, String description, String systemPrompt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.systemPrompt = systemPrompt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    /**
     * 根据 ID 查找角色，未匹配时回退到 DEFAULT
     */
    public static RolePreset of(String id) {
        if (id != null) {
            for (RolePreset role : values()) {
                if (role.id.equalsIgnoreCase(id)) {
                    return role;
                }
            }
        }
        return DEFAULT;
    }
}

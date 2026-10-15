package io.gitee.dqcer.mcdull.ai.model;

/**
 * 会话历史消息视图对象，role 取值：user / assistant
 */
public record ChatMessageVO(String role, String content) {
}

package io.gitee.dqcer.mcdull.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ChatClient 装配示例。
 *
 * <p>Spring AI 中有两层 API：
 * <ul>
 *   <li>{@link org.springframework.ai.chat.model.ChatModel}：底层模型 API（阻塞 call / 流式 stream）</li>
 *   <li>{@link ChatClient}：推荐的高层 API，支持流式、Advisor 拦截链（记忆、RAG、日志等）</li>
 * </ul>
 */
@Configuration
public class ChatClientConfig {

    /** 全局默认系统提示词，请求未指定角色时生效 */
    public static final String DEFAULT_SYSTEM_PROMPT =
            "你是 mcdull-ai 学习项目的 AI 助手，请使用中文回答，语言简洁，适当使用 Markdown 格式。";

    /**
     * 会话记忆：基于内存的滑动窗口，每个会话最多保留 20 条消息。
     * 生产环境可替换为持久化实现（JDBC / Redis 的 ChatMemoryRepository）。
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();
    }

    /**
     * 通过 MessageChatMemoryAdvisor 把会话记忆挂到拦截链上，
     * 请求时通过 advisor 参数 chat_memory_conversation_id 指定会话 ID。
     */
    @Bean
    public ChatClient chatClient(ZhiPuAiChatModel chatModel, ChatMemory chatMemory) {
        return ChatClient.builder(chatModel)
                .defaultSystem(DEFAULT_SYSTEM_PROMPT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}

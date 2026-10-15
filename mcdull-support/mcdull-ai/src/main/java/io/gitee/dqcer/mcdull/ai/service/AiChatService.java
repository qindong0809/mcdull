package io.gitee.dqcer.mcdull.ai.service;

import io.gitee.dqcer.mcdull.ai.enums.RolePreset;
import io.gitee.dqcer.mcdull.ai.model.ChatMessageVO;
import io.gitee.dqcer.mcdull.ai.model.RoleVO;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.zhipuai.ZhiPuAiAssistantMessage;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 对话服务：演示 ChatClient + 会话记忆（Advisor）+ 流式输出 + 系统提示词。
 */
@Service
public class AiChatService {

    private static final Map<MessageType, String> MESSAGE_TYPE_TO_ROLE = Map.of(
            MessageType.USER, "user",
            MessageType.ASSISTANT, "assistant"
    );

    /** 深度思考模式下追加到系统提示词的指令，让模型用 <thinking> 标签输出推理过程 */
    private static final String THINKING_HINT =
            "\n\n【思考要求】请先用 <thinking> 标签详细写出你的思考推理过程，" +
            "然后再给出最终回答。最终回答中不要包含 <thinking> 标签内容。";

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public AiChatService(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    /**
     * 阻塞式对话：等待模型完整返回。
     */
    public String chat(String sessionId, String roleId, String message, boolean enableThinking) {
        return prompt(sessionId, roleId, message, enableThinking).call().content();
    }

    /**
     * 流式对话：以 SSE 事件输出思考过程（event=thinking）与正文（event=content）。
     *
     * <p>思考过程有两种来源：
     * <ul>
     *   <li>原生 reasoning：智谱推理模型（如 glm-z1）通过 {@link ZhiPuAiAssistantMessage#getReasoningContent()} 返回</li>
     *   <li>提示词模拟：开启 enableThinking 后，模型会在正文中用 {@code <thinking>} 标签包裹思考过程，
     *       由前端解析提取（本接口不做拆分，统一走 content 事件）</li>
     * </ul>
     */
    public Flux<ServerSentEvent<String>> streamChat(String sessionId, String roleId, String message,
                                                    boolean enableThinking) {
        return prompt(sessionId, roleId, message, enableThinking)
                .stream()
                .chatResponse()
                .concatMap(cr -> {
                    List<ServerSentEvent<String>> events = new ArrayList<>();
                    cr.getResults().forEach(result -> {
                        AssistantMessage output = (AssistantMessage) result.getOutput();
                        // 原生 reasoning 思考过程
                        if (output instanceof ZhiPuAiAssistantMessage zmsg
                                && zmsg.getReasoningContent() != null
                                && !zmsg.getReasoningContent().isEmpty()) {
                            events.add(ServerSentEvent.<String>builder()
                                    .event("thinking")
                                    .data(zmsg.getReasoningContent())
                                    .build());
                        }
                        // 正文（可能包含 <thinking> 标签，由前端解析）
                        if (output.getText() != null && !output.getText().isEmpty()) {
                            events.add(ServerSentEvent.<String>builder()
                                    .event("content")
                                    .data(output.getText())
                                    .build());
                        }
                    });
                    return Flux.fromIterable(events);
                });
    }

    /**
     * 统一的 Prompt 构造：系统提示词按角色覆盖 + 思考指令 + 通过 Advisor 参数绑定会话记忆。
     */
    private ChatClient.ChatClientRequestSpec prompt(String sessionId, String roleId, String message,
                                                    boolean enableThinking) {
        RolePreset role = RolePreset.of(roleId);
        ChatClient.ChatClientRequestSpec spec = chatClient.prompt();
        String system = role.getSystemPrompt();
        if (enableThinking) {
            system = (system == null ? "" : system) + THINKING_HINT;
        }
        if (system != null && !system.isEmpty()) {
            spec = spec.system(system);
        }
        return spec.user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId));
    }

    /**
     * 会话历史（内存中的滑动窗口消息），按 user / assistant 展示。
     */
    public List<ChatMessageVO> history(String sessionId) {
        return chatMemory.get(sessionId).stream()
                .filter(m -> MESSAGE_TYPE_TO_ROLE.containsKey(m.getMessageType()))
                .map(m -> new ChatMessageVO(MESSAGE_TYPE_TO_ROLE.get(m.getMessageType()), m.getText()))
                .toList();
    }

    /**
     * 清空会话记忆。
     */
    public void clear(String sessionId) {
        chatMemory.clear(sessionId);
    }

    /**
     * 可选角色列表。
     */
    public List<RoleVO> roles() {
        return Arrays.stream(RolePreset.values())
                .map(r -> new RoleVO(r.getId(), r.getName(), r.getDescription()))
                .collect(Collectors.toList());
    }
}

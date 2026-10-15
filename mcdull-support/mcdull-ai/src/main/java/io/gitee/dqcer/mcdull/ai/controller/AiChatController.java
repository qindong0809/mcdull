package io.gitee.dqcer.mcdull.ai.controller;

import io.gitee.dqcer.mcdull.ai.model.ChatMessageVO;
import io.gitee.dqcer.mcdull.ai.model.RoleVO;
import io.gitee.dqcer.mcdull.ai.service.AiChatService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * 面向前端页面的对话接口。
 *
 * <p>基础学习示例见 {@link ChatController}（/ai/generate 与 /ai/generateStream）。
 */
@RestController
@RequestMapping("/ai")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    /**
     * 阻塞式对话（带会话记忆）。enableThinking=true 时模型会输出 &lt;thinking&gt; 思考过程。
     */
    @GetMapping("/chat")
    public Map<String, String> chat(@RequestParam String sessionId,
                                    @RequestParam String message,
                                    @RequestParam(required = false) String role,
                                    @RequestParam(required = false, defaultValue = "false") boolean enableThinking) {
        String content = aiChatService.chat(sessionId, role, message, enableThinking);
        return Map.of("sessionId", sessionId, "content", content);
    }

    /**
     * 流式对话：以 SSE 推送，事件名区分思考过程（thinking）与正文（content）。
     */
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStream(@RequestParam String sessionId,
                                                    @RequestParam String message,
                                                    @RequestParam(required = false) String role,
                                                    @RequestParam(required = false, defaultValue = "false") boolean enableThinking) {
        return aiChatService.streamChat(sessionId, role, message, enableThinking);
    }

    /**
     * 可选角色列表。
     */
    @GetMapping("/roles")
    public List<RoleVO> roles() {
        return aiChatService.roles();
    }

    /**
     * 会话历史。
     */
    @GetMapping("/history/{sessionId}")
    public List<ChatMessageVO> history(@PathVariable String sessionId) {
        return aiChatService.history(sessionId);
    }

    /**
     * 清空会话记忆。
     */
    @DeleteMapping("/history/{sessionId}")
    public Map<String, Object> clearHistory(@PathVariable String sessionId) {
        aiChatService.clear(sessionId);
        return Map.of("success", true);
    }
}

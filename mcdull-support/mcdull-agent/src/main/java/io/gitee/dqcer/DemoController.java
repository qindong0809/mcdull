package io.gitee.dqcer;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.DefaultChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
public class DemoController {

    private final ChatModel chatModel;

    @GetMapping("/chat")
    public Result<String> chat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        return Result.success(chatModel.chat(message));
    }

    @GetMapping("/send-user-chat-and-get-reply")
    public Result<String> sendUserChatAndGetReply(@RequestParam(value = "message", defaultValue = "Hello") String message,
                                                  @RequestParam(value = "ext") String ext) {
        UserMessage userMessage = new UserMessage(
//            TextContent.from("Describe the following image"),
//            ImageContent.from("https://example.com/cat.jpg"),
            TextContent.from(message),
            TextContent.from(ext)
            );
        List<ToolSpecification> toolSpecifications = new ArrayList<>();
        ChatRequest chatRequest = ChatRequest.builder()
            .messages(userMessage)
            .temperature(0.7D)
            .topP(0.1D)
//            .topK(1)
            .frequencyPenalty(0.0D)
            .presencePenalty(0.0D)
            .maxOutputTokens(1024)
            .stopSequences(null)
            .toolSpecifications(toolSpecifications)
            .toolChoice(null)
            .responseFormat(null)
//            .parameters(new DefaultChatRequestParameters()) // you can also set common or provider-specific parameters all at once
            .build();
        ChatResponse chat = chatModel.chat(chatRequest);
        return Result.success(chat.aiMessage().text());
    }

    @GetMapping("/memory-chat")
    public Result<String> memoryChat(@RequestParam(value = "message", defaultValue = "Hello") String message) {
//        ChatMemory chatMemory = TokenWindowChatMemory.withMaxMessages(10);

        return Result.success(chatModel.chat(message));
    }
}

package io.gitee.dqcer.mcdull.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DemoChatController {

    private final ZhiPuAiChatModel chatModel;
    private final ChatClient chatClient;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemResource;

    @Autowired
    public DemoChatController(ZhiPuAiChatModel chatModel, ChatClient chatClient) {
        this.chatModel = chatModel;
        this.chatClient = chatClient;
    }


    @GetMapping("/chat")
    public String chat(@RequestParam String msg) {
        return chatClient.prompt()
            .system("你是一个问答助手")   // 系统提示词
            .user(msg)                    // 用户提示词
            .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, "my-conversation"))
            .call()
            .content();
    }

    /**
     * 静态生成提示词
     */
    @GetMapping("/story")
    public String storyByParam(@RequestParam String role, @RequestParam String topic) {
        PromptTemplate template = new PromptTemplate("你是一个{role}，讲一个关于{topic}的故事");
        Prompt prompt = template.create(Map.of("role", role, "topic", topic));
        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    /**
     * 动态生成提示词
     */
    @GetMapping("/story2")
    public String storyByDynamicTemplate(@RequestParam String name, @RequestParam String voice) {
        String systemText = """
                                你是一个有用的助手，名字是 {name}。
                                请以 {voice} 的风格回复用户。
                            """;
        SystemPromptTemplate systemTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemTemplate.createMessage(Map.of(
            "name", name,
            "voice", voice
        ));
        Prompt prompt = new Prompt(List.of(systemMessage, new UserMessage("你好")));
        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    /**
     * 从文件加载提示词
     */
    @GetMapping("/story3")
    public String storyByResource(@RequestParam String profession, @RequestParam String skill) {

        SystemPromptTemplate systemTemplate = new SystemPromptTemplate(systemResource);
        Message systemMessage = systemTemplate.createMessage(Map.of(
            "profession", profession,
            "skill", skill
        ));
        Prompt prompt = new Prompt(List.of(systemMessage, new UserMessage("你好")));
        return chatModel.call(prompt).getResult().getOutput().getText();
    }

    /**
     * 进阶模式：思维链（Chain of Thought） + 结构化输出
     */
    @GetMapping("/story4")
    public EmailClassification storyByChainOfThought(@RequestParam String emailContent, @RequestParam String skill) {
        // 结构化输出
        return chatClient.prompt()
            .user(u -> u.text("""
                    判断以下邮件是重要还是不重要。
                    邮件内容：{email}

                    请一步一步思考，并解释你的判断理由。
                    """)
                .param("email", emailContent))
            .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, "my-conversation"))
            .call()
            .entity(EmailClassification.class);
    }
}

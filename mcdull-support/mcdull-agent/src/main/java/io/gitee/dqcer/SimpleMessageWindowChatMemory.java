package io.gitee.dqcer;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.ChatMemory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleMessageWindowChatMemory implements ChatMemory {

    private final Object id;
    private final int maxMessages;
    private final List<ChatMessage> messages = new CopyOnWriteArrayList<>();

    public SimpleMessageWindowChatMemory(Object id, int maxMessages) {
        this.id = id;
        this.maxMessages = maxMessages;
    }

    @Override
    public Object id() {
        return id;
    }

    @Override
    public void add(ChatMessage message) {
        messages.add(message);
        // 超过条数，删掉最早消息
        while (messages.size() > maxMessages) {
            messages.remove(0);
        }
    }

    @Override
    public List<ChatMessage> messages() {
        return new ArrayList<>(messages);
    }

    @Override
    public void clear() {
        messages.clear();
    }
}

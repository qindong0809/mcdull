package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantInitProgressVO;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TenantInitProgressPublisher {

    private final Map<String, List<Subscriber>> subscribers = new ConcurrentHashMap<>();
    private final Map<String, Map<Long, Event>> history = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> sequences = new ConcurrentHashMap<>();
    private final ScheduledExecutorService heartbeat = Executors.newSingleThreadScheduledExecutor();

    public TenantInitProgressPublisher() {
        heartbeat.scheduleAtFixedRate(this::sendHeartbeat, 15, 15, TimeUnit.SECONDS);
    }

    public SseEmitter subscribe(String taskNo, Long lastEventId, TenantInitProgressVO snapshot, long timeout) {
        SseEmitter emitter = new SseEmitter(timeout);
        Subscriber subscriber = new Subscriber(emitter);
        subscribers.computeIfAbsent(taskNo, k -> new CopyOnWriteArrayList<>()).add(subscriber);

        AtomicLong sequence = sequences.computeIfAbsent(taskNo, k -> new AtomicLong(0));
        if (lastEventId != null && lastEventId > 0) {
            Map<Long, Event> events = history.getOrDefault(taskNo, Map.of());
            events.forEach((id, event) -> {
                if (id > lastEventId) {
                    send(subscriber, String.valueOf(id), event.name, event.data);
                }
            });
        } else if (snapshot != null) {
            send(subscriber, String.valueOf(sequence.get()), "snapshot", snapshot);
        }

        emitter.onCompletion(() -> remove(taskNo, subscriber));
        emitter.onTimeout(() -> remove(taskNo, subscriber));
        emitter.onError(e -> remove(taskNo, subscriber));
        return emitter;
    }

    public void publish(String taskNo, String eventName, TenantInitProgressVO data) {
        long id = sequences.computeIfAbsent(taskNo, k -> new AtomicLong(0)).incrementAndGet();
        Event event = new Event(eventName, data);
        history.computeIfAbsent(taskNo, k -> new ConcurrentHashMap<>()).put(id, event);
        List<Subscriber> list = subscribers.getOrDefault(taskNo, List.of());
        for (Subscriber subscriber : list) {
            send(subscriber, String.valueOf(id), eventName, data);
        }
    }

    public void closeTask(String taskNo) {
        subscribers.remove(taskNo);
    }

    private void remove(String taskNo, Subscriber subscriber) {
        List<Subscriber> list = subscribers.get(taskNo);
        if (list != null) {
            list.remove(subscriber);
            if (list.isEmpty()) {
                subscribers.remove(taskNo);
            }
        }
    }

    private void sendHeartbeat() {
        subscribers.forEach((taskNo, list) -> {
            for (Subscriber subscriber : list) {
                try {
                    subscriber.emitter.send(SseEmitter.event().comment("hb"));
                } catch (IOException | IllegalStateException ignored) {
                    remove(taskNo, subscriber);
                }
            }
        });
    }

    private void send(Subscriber subscriber, String id, String name, Object data) {
        try {
            subscriber.emitter.send(SseEmitter.event().id(id).name(name).data(data));
        } catch (IOException | IllegalStateException ignored) {
            // 订阅端断开时由 onCompletion/onError 清理
        }
    }

    @PreDestroy
    public void shutdown() {
        heartbeat.shutdownNow();
    }

    private static class Subscriber {
        private final SseEmitter emitter;

        private Subscriber(SseEmitter emitter) {
            this.emitter = emitter;
        }
    }

    private static class Event {
        private final String name;
        private final TenantInitProgressVO data;

        private Event(String name, TenantInitProgressVO data) {
            this.name = name;
            this.data = data;
        }
    }
}

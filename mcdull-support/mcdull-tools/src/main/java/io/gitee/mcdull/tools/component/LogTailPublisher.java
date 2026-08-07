package io.gitee.mcdull.tools.component;

import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.mcdull.tools.config.LogProperties;
import io.gitee.mcdull.tools.web.domain.LogPageVO;
import io.gitee.mcdull.tools.web.service.LogFileService;
import io.gitee.mcdull.tools.web.service.source.LogReader;
import io.gitee.mcdull.tools.web.service.source.LogSource;
import io.gitee.mcdull.tools.web.service.source.LogSourceRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Follows the active log file once and fans new lines out to every subscriber.
 * <p>
 * One shared reader rather than one per connection: tailing the same file from every request thread
 * multiplies reads by the number of viewers and leaks a thread per connection.
 * <p>
 * Fan out happens on the tail thread. A subscriber whose socket buffer is full therefore slows the
 * others down. That is an accepted trade for an internal tool with a handful of viewers, and a stalled
 * connection eventually throws and gets dropped.
 *
 * @author dqcer
 */
@Slf4j
@Component
public class LogTailPublisher {

    /**
     * Upper bound for one pump cycle, keeps a large burst from being read into memory at once.
     */
    private static final int MAX_PUMP_BYTES = 1024 * 1024;

    private static final int MAX_PENDING_BYTES = 1024 * 1024;

    private static final byte LF = '\n';

    private static final byte CR = '\r';

    private static final byte[] EMPTY = new byte[0];

    private final List<Subscriber> subscribers = new CopyOnWriteArrayList<>();

    @Resource
    private LogProperties logProperties;

    @Resource
    private LogFileService logFileService;

    @Resource
    private LogSourceRegistry logSourceRegistry;

    private ScheduledExecutorService scheduler;

    /**
     * Offset already published. Only touched by the tail thread.
     */
    private long position;

    /**
     * Trailing bytes of a line that has not been terminated yet.
     */
    private byte[] pending = EMPTY;

    @PostConstruct
    public void start() {
        // Start at the end of the file. Without this a client connecting before the first pump cycle
        // would be sent the whole file as if it were new output.
        position = this.currentLength();
        scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "log-tail");
            thread.setDaemon(true);
            return thread;
        });
        long interval = Math.max(100L, logProperties.getTailIntervalMillis());
        scheduler.scheduleWithFixedDelay(this::pump, interval, interval, TimeUnit.MILLISECONDS);
        long heartbeat = Math.max(1000L, logProperties.getHeartbeatMillis());
        scheduler.scheduleAtFixedRate(this::heartbeat, heartbeat, heartbeat, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void stop() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        subscribers.forEach(subscriber -> subscriber.emitter.complete());
        subscribers.clear();
    }

    /**
     * Opens a live stream. The last page of the file is replayed first so the viewer starts with
     * context instead of a blank pane, then new lines arrive as they are written.
     *
     * @param keyword case insensitive substring filter, blank streams everything
     * @return the emitter to hand back to the client
     */
    public SseEmitter subscribe(String keyword) {
        return this.subscribe(null, keyword);
    }

    /**
     * Opens a live stream for a specific target.
     *
     * @param target  target id, blank uses the default
     * @param keyword case insensitive substring filter, blank streams everything
     * @return the emitter to hand back to the client
     */
    public SseEmitter subscribe(String target, String keyword) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        LogSource source = logSourceRegistry.resolve(target);
        Subscriber subscriber = new Subscriber(emitter,
                StrUtil.isBlank(keyword) ? null : keyword.toLowerCase(Locale.ROOT),
                target, source);
        emitter.onCompletion(() -> subscribers.remove(subscriber));
        emitter.onTimeout(() -> {
            subscribers.remove(subscriber);
            emitter.complete();
        });
        emitter.onError(throwable -> subscribers.remove(subscriber));

        this.replay(subscriber);
        // A line written between the replay and this add is missed. Widening that window costs a lock
        // on the hot path, and one skipped line at connect time does not justify it.
        subscribers.add(subscriber);
        return emitter;
    }

    /**
     * @return number of live subscribers
     */
    public int subscriberCount() {
        return subscribers.size();
    }

    private void replay(Subscriber subscriber) {
        try {
            LogPageVO page = logFileService.readBackward(subscriber.target, null, null,
                    logProperties.getPageLines(), subscriber.keyword);
            for (String line : page.getLines()) {
                subscriber.emitter.send(line);
            }
        } catch (Exception e) {
            // No history is not a reason to refuse the live stream.
            LogHelp.warn(log, "failed to replay log history: {}", e.getMessage());
        }
    }

    private long currentLength() {
        try (LogReader reader = logSourceRegistry.resolve(null).open(null)) {
            return reader.size();
        } catch (Exception e) {
            // Not configured yet or the file is missing, the first pump cycle will settle it.
            return 0L;
        }
    }

    private void pump() {
        // Reopened every cycle on purpose: after a rotation the old handle would keep pointing at the
        // file that was moved away.
        try (LogReader reader = logSourceRegistry.resolve(null).open(null)) {
            long length = reader.size();
            if (length < position) {
                // Rotated or truncated, start over from the head of the new file.
                position = 0;
                pending = EMPTY;
            }
            if (subscribers.isEmpty()) {
                // Nobody is watching, skip to the end so the first subscriber is not flooded with
                // everything written while the page was closed. It gets a replayed page instead.
                position = length;
                pending = EMPTY;
                return;
            }
            if (length == position) {
                return;
            }
            int size = (int) Math.min(length - position, MAX_PUMP_BYTES);
            byte[] buffer = new byte[size];
            reader.readFully(position, buffer, 0, size);
            position += size;
            this.publish(buffer);
        } catch (Exception e) {
            // The file may not exist yet or be briefly unavailable during rotation, retry next cycle.
            LogHelp.warn(log, "failed to tail log file: {}", e.getMessage());
        }
    }

    private void publish(byte[] buffer) {
        Charset charset = logSourceRegistry.resolve(null).charset();
        List<String> lines = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] != LF) {
                continue;
            }
            lines.add(this.toLine(buffer, start, i, charset));
            start = i + 1;
        }
        // Whatever follows the last terminator belongs to a line still being written.
        int tail = buffer.length - start;
        if (tail <= 0) {
            pending = EMPTY;
        } else if (pending.length + tail <= MAX_PENDING_BYTES) {
            byte[] merged = new byte[pending.length + tail];
            System.arraycopy(pending, 0, merged, 0, pending.length);
            System.arraycopy(buffer, start, merged, pending.length, tail);
            pending = merged;
        } else {
            // An unterminated line past the cap is dropped. Holding it would grow without bound, and
            // keeping a stale buffer would splice it onto the front of the next real line.
            pending = EMPTY;
        }
        for (String line : lines) {
            this.send(line);
        }
    }

    private String toLine(byte[] buffer, int from, int to, Charset charset) {
        int length = to - from;
        byte[] merged = new byte[pending.length + length];
        System.arraycopy(pending, 0, merged, 0, pending.length);
        System.arraycopy(buffer, from, merged, pending.length, length);
        pending = EMPTY;
        int end = merged.length;
        if (end > 0 && merged[end - 1] == CR) {
            end--;
        }
        return new String(merged, 0, end, charset);
    }

    private void send(String line) {
        String lower = null;
        for (Subscriber subscriber : subscribers) {
            if (subscriber.keyword != null) {
                if (lower == null) {
                    lower = line.toLowerCase(Locale.ROOT);
                }
                if (!lower.contains(subscriber.keyword)) {
                    continue;
                }
            }
            try {
                subscriber.emitter.send(line);
            } catch (Exception e) {
                subscribers.remove(subscriber);
                subscriber.emitter.completeWithError(e);
            }
        }
    }

    private void heartbeat() {
        for (Subscriber subscriber : subscribers) {
            try {
                subscriber.emitter.send(SseEmitter.event().comment("hb"));
            } catch (Exception e) {
                subscribers.remove(subscriber);
                subscriber.emitter.completeWithError(e);
            }
        }
    }

    private static final class Subscriber {

        private final SseEmitter emitter;

        /**
         * Lower cased filter, null streams every line.
         */
        private final String keyword;

        private final String target;

        private final LogSource source;

        private Subscriber(SseEmitter emitter, String keyword, String target, LogSource source) {
            this.emitter = emitter;
            this.keyword = keyword;
            this.target = target;
            this.source = source;
        }
    }
}

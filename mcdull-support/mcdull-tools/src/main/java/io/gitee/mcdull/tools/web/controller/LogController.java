package io.gitee.mcdull.tools.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.Tailer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
public class LogController {

    private final Set<SseEmitter> emitters = Collections.synchronizedSet(new HashSet<>());

    @SaIgnore
    @GetMapping(value = "/logs-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamLogs() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        Tailer tailer = new Tailer(FileUtil.file("D:\\var\\log\\mcdull-uac-provider\\out.log"), line -> {
            try {
                emitter.send(line);
//                emitter.complete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, 2);
        tailer.start();
        emitter.complete();



//        emitters.add(emitter);
//        emitter.onCompletion(() -> emitters.remove(emitter));
//        emitter.onTimeout(() -> emitters.remove(emitter));
//        new Thread(() -> {
//            for (int i = 0; i < 100; i++) {
//                try {
//                    emitter.send("data: " + i + "\n\n");
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            emitter.complete();
//        }).start();
        return emitter;
    }

    @SaIgnore
    public void broadcast(String message) {
        synchronized (emitters) {
            emitters.forEach(emitter -> {
                try {
                    emitter.send(message);
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            });
        }
    }
}

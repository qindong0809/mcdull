package io.gitee.mcdull.tools.component;

import io.gitee.mcdull.tools.web.controller.LogController;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class LogMonitor {

    @Value("${log.monitor.enable:false}")
    private String logPath;

    @Resource
    private LogController logController;

    private long lastReadPosition = 0;

    @PostConstruct
    public void init() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                checkUpdates();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void checkUpdates() throws IOException {
        RandomAccessFile file = new RandomAccessFile("D:\\var\\log\\mcdull-uac-provider\\out.log", "r");
        long fileLength = file.length();

        if (fileLength < lastReadPosition) {
            lastReadPosition = 0; // 处理日志轮转
        }

        if (fileLength > lastReadPosition) {
            file.seek(lastReadPosition);
            String line;
            while ((line = file.readLine()) != null) {
                String utf8Line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                logController.broadcast(utf8Line + "\n");
            }
            lastReadPosition = file.getFilePointer();
        }
        file.close();
    }
}

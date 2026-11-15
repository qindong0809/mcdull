package io.gitee.dqcer.mcdull.framework.base.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * 通用 webhook 消息发送器，钉钉 / 企业微信 / 飞书群机器人共用。
 * <p>
 * 无 Spring、无第三方 HTTP 依赖，仅用 JDK {@code java.net.http.HttpClient}。
 * 构建期（mcdull-devtools-notify plugin）与运行期（logback appender、scheduler callback）共用。
 * <p>
 * 契约：任何失败都不抛异常，只 warn 日志；返回 false 表示未收到 2xx。
 * 通知类工具的通用准则是"绝不打断被通知方所在的流程"。
 *
 * @author dqcer
 * @since 2026-11-15
 */
public final class WebhookSender {

    private static final Logger log = LoggerFactory.getLogger(WebhookSender.class);

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(2);

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);

    private static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(CONNECT_TIMEOUT)
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    private WebhookSender() {
    }

    /**
     * POST JSON 到目标 webhook。
     *
     * @param webhook  目标 URL，空则跳过
     * @param jsonBody 序列化后的 JSON 请求体
     * @return true 表示 HTTP 返回 2xx
     */
    public static boolean sendJson(String webhook, String jsonBody) {
        if (webhook == null || webhook.isBlank()) {
            log.warn("[mcdull-notify] webhook 未配置，跳过发送");
            return false;
        }
        if (jsonBody == null) {
            log.warn("[mcdull-notify] json body 为 null，跳过发送");
            return false;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhook))
                    .timeout(REQUEST_TIMEOUT)
                    .header("Content-Type", CONTENT_TYPE_JSON)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status / 100 != 2) {
                log.warn("[mcdull-notify] webhook HTTP {} body={}", status, abbreviate(response.body()));
                return false;
            }
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[mcdull-notify] webhook 发送被中断: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.warn("[mcdull-notify] webhook 发送失败: {}", e.toString());
            return false;
        }
    }

    private static String abbreviate(String s) {
        if (s == null) {
            return null;
        }
        return s.length() <= 200 ? s : s.substring(0, 200) + "...";
    }
}

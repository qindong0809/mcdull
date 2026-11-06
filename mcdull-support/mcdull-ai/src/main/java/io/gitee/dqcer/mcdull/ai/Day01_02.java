package io.gitee.dqcer.mcdull.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Day01_02 {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ArrayNode messages = OBJECT_MAPPER.createArrayNode();
    private static final HttpClient CLIENT = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10))
        .build();
    private static final String API_URL = "http://localhost:11434/api/chat";
    private static final String MODEL = "qwen2.5:7b-instruct-q4_K_M";

    public static void main(String[] args) throws IOException, InterruptedException {
        ObjectNode systemMsg = OBJECT_MAPPER.createObjectNode();
        systemMsg.put("role", "system");
        systemMsg.put("content", "你可以调用工具来回答问题。当需要查询天气时，使用get_current_weather工具，不要编造数据。");
        systemMsg.put("content", "你可以调用工具来回答问题。当需要查询天气时，使用get_current_weather工具，不要编造数据。");
        messages.add(systemMsg);

        chat("成都今天天气怎么样？");
    }

    private static void chat(String userContent) throws IOException, InterruptedException {
        // 添加用户消息
        ObjectNode userMsg = OBJECT_MAPPER.createObjectNode();
        userMsg.put("role", "user");
        userMsg.put("content", userContent);
        messages.add(userMsg);

        // 定义工具清单（给模型的说明书）
        ArrayNode tools = buildTools();

        ObjectNode reqBody = OBJECT_MAPPER.createObjectNode();
        reqBody.put("model", MODEL);
        reqBody.set("messages", messages);
        reqBody.set("tools", tools);
        reqBody.put("stream", false);
        ObjectNode options = OBJECT_MAPPER.createObjectNode();
        options.put("temperature", 0.1);
        reqBody.set("options", options);

        String jsonBody = OBJECT_MAPPER.writeValueAsString(reqBody);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode respNode = OBJECT_MAPPER.readTree(response.body());
        System.out.println("【AI回复】：" + respNode.get("message").get("content").asText());
        JsonNode aiMessage = respNode.get("message");

        // ========== 重点判断：是否返回 tool_calls ==========
        if (aiMessage.has("tool_calls") && aiMessage.get("tool_calls").isArray() && aiMessage.get("tool_calls").size() > 0) {
            System.out.println("\n>>> 模型触发工具调用 tool_calls");
            // 1. 把带tool_calls的assistant消息存入上下文，必不可少
            messages.add(aiMessage);

            JsonNode toolCalls = aiMessage.get("tool_calls");
            for (JsonNode call : toolCalls) {
                String toolCallId = call.get("id").asText();
                String funcName = call.get("function").get("name").asText();
                JsonNode arguments = call.get("function").get("arguments");
                System.out.println("调用工具：" + funcName + "，参数：" + arguments);

                // 2. 调度本地方法执行工具
                String toolResult = dispatchFunction(funcName, arguments);

                // 3. 构造 role=tool 的返回消息，塞回 messages
                ObjectNode toolMsg = OBJECT_MAPPER.createObjectNode();
                toolMsg.put("role", "tool");
                toolMsg.put("tool_call_id", toolCallId);
                toolMsg.put("content", toolResult);
                messages.add(toolMsg);
            }
            // 4. 递归再次调用chat，模型读取工具结果，生成最终回答
            chat("");
        } else {
            // 正常文本回答分支
            String content = aiMessage.get("content").asText();
            System.out.println("\n【AI最终回答】：" + content);
            messages.add(aiMessage);
        }
    }

    // 工具路由分发器：根据函数名，跑不同逻辑
    private static String dispatchFunction(String funcName, JsonNode args) {
        if ("get_current_weather".equals(funcName)) {
            String city = args.get("city").asText();
            // 这里你可以对接真实天气API，现在模拟返回
            return "{" +
                "\"city\":\"" + city + "\"," +
                "\"temp\":\"22℃\"," +
                "\"weather\":\"多云\"" +
                "}";
        }
        return "未知工具";
    }

    // 构建tools定义
    private static ArrayNode buildTools() {
        ArrayNode tools = OBJECT_MAPPER.createArrayNode();
        ObjectNode weatherTool = OBJECT_MAPPER.createObjectNode();
        weatherTool.put("type", "function");
        ObjectNode func = OBJECT_MAPPER.createObjectNode();
        func.put("name", "get_current_weather");
        func.put("description", "查询指定城市的实时天气");
        ObjectNode params = OBJECT_MAPPER.createObjectNode();
        params.put("type", "object");
        ArrayNode required = OBJECT_MAPPER.createArrayNode();
        required.add("city");
        params.set("required", required);
        ObjectNode prop = OBJECT_MAPPER.createObjectNode();
        ObjectNode cityProp = OBJECT_MAPPER.createObjectNode();
        cityProp.put("type", "string");
        cityProp.put("description", "城市名称");
        prop.set("city", cityProp);
        params.set("properties", prop);
        func.set("parameters", params);
        weatherTool.set("function", func);
        tools.add(weatherTool);
        return tools;
    }
}


package io.gitee.dqcer.mcdull.ai;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Day01_01 {

    public static void main(String[] args) throws IOException, InterruptedException {
                String apiUrl = "http://localhost:11434/api/generate";
                String requestBody = """
                                       {
                                           "model": "qwen2.5:7b-instruct-q4_K_M",
                                           "prompt": "介绍一下Ollama",
                                           "stream": false,
                                           "max_tokens": 100,
                                           "temperature": 0.1,
                                           "top_p": 0.1,
                                           "n": 1,
                                           "stop": null,
                                           "presence_penalty": 0.1,
                                           "frequency_penalty": 0.9
                                       }
                                   """;
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Response: " + response.body());
                // Response: {"model":"qwen2.5:7b-instruct-q4_K_M","created_at":"2026-11-03T07:02:26.8710129Z","response":"Ollama是一家专注于开发和部署生成式人工智能模型的公司，其目标是使强大的人工智能技术更加易于访问和使用。该公司提供了一个基于生成式人工智能模型的平台，旨在帮助开发者和研究人员创建各种应用，如自然语言处理、内容生成、对话系统等。Ollama的模型是基于开源技术构建的，强调透明度和可定制性，允许用户根据自己的需求调整和优化模型。\n\nOllama提供了模型和开发工具，让没有深度学习专业知识的用户也能参与到生成式人工智能模型的开发过程中来。通过这种方式，Ollama希望推动人工智能技术的普及和创新，促进其在各个行业中的应用和发展。请注意，具体的业务细节和技术实现可能会随时间发生变化，建议访问Ollama官方网站获取最新信息。","done":true,"done_reason":"stop","context":[151644,8948,198,2610,525,1207,16948,11,3465,553,54364,14817,13,1446,525,264,10950,17847,13,151645,198,151644,872,198,109432,46,654,3029,151645,198,151644,77091,198,46,654,3029,105783,107782,100013,33108,102121,43959,28330,104455,104949,9370,73218,3837,41146,100160,20412,32555,104795,104455,99361,101896,114660,104925,33108,37029,1773,104777,99553,104059,104210,43959,28330,104455,104949,9370,100133,3837,106166,100364,113129,33108,113299,50377,100646,99892,3837,29524,99795,102064,54542,5373,43815,43959,5373,105051,72448,49567,1773,46,654,3029,9370,104949,20412,104210,115462,99361,104004,9370,3837,104046,105279,26381,33108,30440,104790,33071,3837,102496,20002,100345,100005,100354,101921,33108,103983,104949,3407,46,654,3029,104257,104949,33108,100013,102011,3837,99258,80443,102217,100134,112104,9370,20002,104425,113965,43959,28330,104455,104949,9370,100013,101925,36407,1773,67338,115550,3837,46,654,3029,99880,101890,104455,99361,9370,105258,33108,99656,3837,101902,41146,18493,101284,99717,101047,99892,106344,1773,118271,3837,108247,103923,104449,108800,101884,104309,99411,20450,113838,3837,101898,104925,46,654,3029,108983,45912,104027,27369,1773],"total_duration":4789539300,"load_duration":11476800,"prompt_eval_count":33,"prompt_eval_cached_count":32,"prompt_eval_duration":337063000,"eval_count":164,"eval_duration":4414020000}

        }
    }


package io.gitee.dqcer.mcdull.framework.external.proxy;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitee.dqcer.mcdull.framework.external.annotation.Body;
import io.gitee.dqcer.mcdull.framework.external.annotation.ExternalGet;
import io.gitee.dqcer.mcdull.framework.external.annotation.ExternalPost;
import io.gitee.dqcer.mcdull.framework.external.annotation.ExternalPut;
import io.gitee.dqcer.mcdull.framework.external.annotation.PathVar;
import io.gitee.dqcer.mcdull.framework.external.annotation.QueryParam;
import io.gitee.dqcer.mcdull.framework.external.exception.ExternalApiException;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dynamic proxy handler for {@code @ExternalApi} interfaces.
 * Resolves annotations, builds HTTP requests, and deserializes responses.
 *
 * @author dqcer
 * @since 1.0.0
 */
public class ExternalClientProxy implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(ExternalClientProxy.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final String clientName;
    private final String baseUrl;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ExternalClientProxy(String clientName, String baseUrl, OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.clientName = clientName;
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Handle Object methods
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        // Determine HTTP method and path
        String httpMethod;
        String pathTemplate;

        ExternalPost postAnno = method.getAnnotation(ExternalPost.class);
        ExternalGet getAnno = method.getAnnotation(ExternalGet.class);
        ExternalPut putAnno = method.getAnnotation(ExternalPut.class);

        if (postAnno != null) {
            httpMethod = "POST";
            pathTemplate = postAnno.value();
        } else if (getAnno != null) {
            httpMethod = "GET";
            pathTemplate = getAnno.value();
        } else if (putAnno != null) {
            httpMethod = "PUT";
            pathTemplate = putAnno.value();
        } else {
            throw new ExternalApiException(clientName, "Method " + method.getName() + " has no HTTP mapping annotation");
        }

        // Parse parameters
        Parameter[] parameters = method.getParameters();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Map<String, String> pathVars = new LinkedHashMap<>();
        Map<String, String> queryParams = new LinkedHashMap<>();
        Object body = null;

        for (int i = 0; i < parameters.length; i++) {
            for (Annotation anno : paramAnnotations[i]) {
                if (anno instanceof PathVar pathVar) {
                    pathVars.put(pathVar.value(), String.valueOf(args[i]));
                } else if (anno instanceof QueryParam queryParam) {
                    if (args[i] != null) {
                        queryParams.put(queryParam.value(), String.valueOf(args[i]));
                    }
                } else if (anno instanceof Body) {
                    body = args[i];
                }
            }
        }

        // Build URL
        String path = pathTemplate;
        for (Map.Entry<String, String> entry : pathVars.entrySet()) {
            path = path.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        String rawUrl = baseUrl + path;
        HttpUrl parsedUrl = HttpUrl.parse(rawUrl);
        if (parsedUrl == null) {
            throw new ExternalApiException(clientName, "Invalid URL: " + rawUrl);
        }
        HttpUrl.Builder urlBuilder = parsedUrl.newBuilder();
        queryParams.forEach(urlBuilder::addQueryParameter);
        String url = urlBuilder.build().toString();

        // Build request
        Request.Builder requestBuilder = new Request.Builder().url(url);
        switch (httpMethod) {
            case "POST" -> requestBuilder.post(buildBody(body));
            case "PUT" -> requestBuilder.put(buildBody(body));
            case "GET" -> requestBuilder.get();
        }

        log.debug("[External:{}] {} {}", clientName, httpMethod, url);

        // Execute
        try (Response response = httpClient.newCall(requestBuilder.build()).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                throw new ExternalApiException(clientName, response.code(),
                        httpMethod + " " + url + " failed: HTTP " + response.code() + " " + errorBody);
            }

            // Deserialize response
            Type returnType = method.getGenericReturnType();
            if (returnType == void.class || returnType == Void.class) {
                return null;
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            JavaType javaType = objectMapper.getTypeFactory().constructType(returnType);
            return objectMapper.readValue(responseBody, javaType);
        } catch (ExternalApiException e) {
            throw e;
        } catch (IOException e) {
            throw new ExternalApiException(clientName, httpMethod + " " + url + " failed", e);
        }
    }

    private RequestBody buildBody(Object body) throws Exception {
        if (body == null) {
            return RequestBody.create(JSON, "{}");
        }
        if (body instanceof byte[] bytes) {
            return RequestBody.create(MediaType.parse("application/octet-stream"), bytes);
        }
        return RequestBody.create(JSON, objectMapper.writeValueAsString(body));
    }
}

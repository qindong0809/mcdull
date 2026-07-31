package io.gitee.dqcer.mcdull.framework.external.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitee.dqcer.mcdull.framework.external.auth.ApiKeyAuthStrategy;
import io.gitee.dqcer.mcdull.framework.external.auth.AuthStrategy;
import io.gitee.dqcer.mcdull.framework.external.auth.CredentialsAuthStrategy;
import io.gitee.dqcer.mcdull.framework.external.auth.JwtAccountAuthStrategy;
import io.gitee.dqcer.mcdull.framework.external.auth.NoneAuthStrategy;
import io.gitee.dqcer.mcdull.framework.external.http.ExternalHttpClientFactory;
import io.gitee.dqcer.mcdull.framework.external.proxy.ExternalClientProxy;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * Spring FactoryBean that creates a dynamic proxy for an {@code @ExternalApi} interface.
 *
 * @author dqcer
 * @since 1.0.0
 */
public class ExternalClientFactoryBean implements FactoryBean<Object> {

    private Class<?> apiInterface;
    private String clientName;

    @Resource
    private ExternalClientProperties properties;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Object getObject() {
        ExternalClientProperties.ClientConfig config = properties.getClients().get(clientName);
        if (config == null) {
            throw new IllegalStateException("No configuration found for external client: " + clientName
                    + ". Add mcdull.external.clients." + clientName + " to your application.yml");
        }

        AuthStrategy authStrategy = createAuthStrategy(config);
        OkHttpClient httpClient = ExternalHttpClientFactory.build(config, authStrategy);

        ExternalClientProxy handler = new ExternalClientProxy(
                clientName, config.getBaseUrl(), httpClient, objectMapper);

        return Proxy.newProxyInstance(
                apiInterface.getClassLoader(),
                new Class[]{apiInterface},
                handler);
    }

    @Override
    public Class<?> getObjectType() {
        return apiInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private AuthStrategy createAuthStrategy(ExternalClientProperties.ClientConfig config) {
        ExternalClientProperties.AuthConfig auth = config.getAuth();
        return switch (auth.getType()) {
            case "jwt-account" -> new JwtAccountAuthStrategy(
                    config.getBaseUrl(),
                    auth.getTokenPath(),
                    auth.getAccountName(),
                    auth.getSecretKey(),
                    objectMapper);
            case "credentials" -> new CredentialsAuthStrategy(
                    config.getBaseUrl(),
                    auth.getTokenPath(),
                    auth.getUsername(),
                    auth.getPassword(),
                    auth.getTokenField(),
                    auth.getExtraParams(),
                    objectMapper);
            case "api-key" -> new ApiKeyAuthStrategy(auth.getHeaderName(), auth.getApiKey());
            default -> new NoneAuthStrategy();
        };
    }

    // --- Setters for Spring injection ---

    public void setApiInterface(Class<?> apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}

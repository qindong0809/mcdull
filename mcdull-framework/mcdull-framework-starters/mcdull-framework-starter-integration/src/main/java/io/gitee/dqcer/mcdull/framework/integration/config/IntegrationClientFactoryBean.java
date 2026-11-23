package io.gitee.dqcer.mcdull.framework.integration.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gitee.dqcer.mcdull.framework.integration.auth.ApiKeyAuthStrategy;
import io.gitee.dqcer.mcdull.framework.integration.auth.AuthStrategy;
import io.gitee.dqcer.mcdull.framework.integration.auth.CredentialsAuthStrategy;
import io.gitee.dqcer.mcdull.framework.integration.auth.JwtAccountAuthStrategy;
import io.gitee.dqcer.mcdull.framework.integration.auth.NoneAuthStrategy;
import io.gitee.dqcer.mcdull.framework.integration.http.IntegrationHttpClientFactory;
import io.gitee.dqcer.mcdull.framework.integration.proxy.IntegrationClientProxy;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * Spring FactoryBean that creates a dynamic proxy for an {@code @IntegrationApi} interface.
 *
 * @author dqcer
 * @since 1.0.0
 */
public class IntegrationClientFactoryBean implements FactoryBean<Object> {

    private Class<?> apiInterface;
    private String clientName;

    @Resource
    private IntegrationClientProperties properties;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Object getObject() {
        IntegrationClientProperties.ClientConfig config = properties.getClients().get(clientName);
        if (config == null) {
            throw new IllegalStateException("No configuration found for integration client: " + clientName
                    + ". Add mcdull.integration.clients." + clientName + " to your application.yml");
        }

        AuthStrategy authStrategy = createAuthStrategy(config);
        OkHttpClient httpClient = IntegrationHttpClientFactory.build(config, authStrategy);

        IntegrationClientProxy handler = new IntegrationClientProxy(
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

    private AuthStrategy createAuthStrategy(IntegrationClientProperties.ClientConfig config) {
        IntegrationClientProperties.AuthConfig auth = config.getAuth();
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

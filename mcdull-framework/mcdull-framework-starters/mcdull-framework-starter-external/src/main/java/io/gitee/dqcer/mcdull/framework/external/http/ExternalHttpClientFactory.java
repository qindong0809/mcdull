package io.gitee.dqcer.mcdull.framework.external.http;

import io.gitee.dqcer.mcdull.framework.external.auth.AuthStrategy;
import io.gitee.dqcer.mcdull.framework.external.config.ExternalClientProperties;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Factory for building OkHttpClient instances with auth and logging interceptors.
 *
 * @author dqcer
 * @since 1.0.0
 */
public class ExternalHttpClientFactory {

    private static final Logger log = LoggerFactory.getLogger(ExternalHttpClientFactory.class);

    /**
     * Build an OkHttpClient for a specific external system.
     */
    public static OkHttpClient build(ExternalClientProperties.ClientConfig config, AuthStrategy authStrategy) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor(authStrategy));

        if (config.getRetry().getMaxAttempts() > 0) {
            builder.addInterceptor(new RetryInterceptor(config.getRetry()));
        }

        if (config.isLogEnabled()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(log::debug);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        return builder.build();
    }

    /**
     * Interceptor that injects authentication headers from the AuthStrategy.
     */
    private static class AuthInterceptor implements Interceptor {
        private final AuthStrategy authStrategy;

        AuthInterceptor(AuthStrategy authStrategy) {
            this.authStrategy = authStrategy;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder requestBuilder = chain.request().newBuilder();
            Map<String, String> headers = authStrategy.getAuthHeaders();
            headers.forEach(requestBuilder::header);
            return chain.proceed(requestBuilder.build());
        }
    }

    /**
     * Interceptor that retries failed requests with exponential backoff.
     * Note: This works as an Application Interceptor where each retry creates a new call.
     */
    private static class RetryInterceptor implements Interceptor {
        private final ExternalClientProperties.RetryConfig retryConfig;

        RetryInterceptor(ExternalClientProperties.RetryConfig retryConfig) {
            this.retryConfig = retryConfig;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = null;
            IOException lastException = null;

            for (int attempt = 0; attempt <= retryConfig.getMaxAttempts(); attempt++) {
                try {
                    if (response != null) {
                        response.close();
                    }
                    response = chain.proceed(request);
                    if (response.isSuccessful()) {
                        return response;
                    }
                    // Non-retryable status codes
                    if (response.code() >= 400 && response.code() < 500) {
                        return response;
                    }
                    if (attempt >= retryConfig.getMaxAttempts()) {
                        return response;
                    }
                    response.close();
                } catch (IOException e) {
                    lastException = e;
                    if (attempt >= retryConfig.getMaxAttempts()) {
                        throw e;
                    }
                }

                try {
                    long backoff = retryConfig.getBackoffMs() * (1L << attempt);
                    log.warn("[ExternalRetry] Attempt {}/{} failed, retrying in {}ms",
                            attempt + 1, retryConfig.getMaxAttempts(), backoff);
                    Thread.sleep(backoff);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    if (lastException != null) {
                        throw lastException;
                    }
                    throw new IOException("Retry interrupted", ie);
                }
            }
            if (lastException != null) {
                throw lastException;
            }
            return response;
        }
    }
}

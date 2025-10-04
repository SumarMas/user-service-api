package com.platform.user_service.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

/**
 * Configuration class for RestTemplate settings.
 */
@Configuration
public class RestTemplateConfig {
    /**
     * Timeout duration for the RestTemplate in milliseconds.
     */
    private static final int TIMEOUT = 1000;

    /**
     * Creates a RestTemplate bean with custom timeout settings.
     *
     * @param builder the RestTemplateBuilder used to configure the RestTemplate
     * @param interceptors a list of ClientHttpRequestInterceptor instances to be added to the RestTemplate
     * @return a RestTemplate instance with the specified timeout settings
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, List<ClientHttpRequestInterceptor> interceptors) {
        return builder
                .setConnectTimeout(Duration.ofMillis(TIMEOUT))
                .setReadTimeout(Duration.ofMillis(TIMEOUT))
                .interceptors(interceptors)
                .build();
    }
}

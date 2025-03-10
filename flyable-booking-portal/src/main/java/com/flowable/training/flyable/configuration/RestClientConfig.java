package com.flowable.training.flyable.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Configuration for the REST clients used in the Flyable application.
 */
@Configuration
public class RestClientConfig {


    @Value("${flyable.flowable-url}")
    private String flowableApiUrl;

    @Value("${flyable.flowable-admin-user}")
    private String adminUser;

    @Value("${flyable.flowable-admin-password}")
    private String adminPassword;

    /**
     * Creates a REST client that uses the current user's credentials.
     * Since we're just using a "mock" implementation of JWT here, you would probably solve this
     * more elegantly in a productive setup.
     *
     * @return The REST client
     */
    @Primary
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(flowableApiUrl)
                .requestInterceptor((request, body, execution) -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication != null && authentication.isAuthenticated()) {
                        String username = authentication.getName();
                        // We hope it's clear that you don't do this in production :)
                        // Usually, we would make use of OAuth2 or a system user
                        String password = username.equals("admin") ? "test" : "training";

                        String auth = username + ":" + password;
                        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
                        String authHeader = "Basic " + new String(encodedAuth);

                        request.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
                    }
                    return execution.execute(request, body);
                })
                .build();
    }

    /**
     * Creates a REST client that uses the admin user's credentials.
     *
     * @return The REST client
     */
    @Bean
    public RestClient adminRestClient() {
        return RestClient.builder()
                .baseUrl(flowableApiUrl)
                .requestInterceptor((request, body, execution) -> {
                    String auth = adminUser + ":" + adminPassword;
                    byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
                    String authHeader = "Basic " + new String(encodedAuth);
                    request.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
                    return execution.execute(request, body);
                })
                .build();
    }

}

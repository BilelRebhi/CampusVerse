package com.campusverse.event.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

/**
 * One RestClient per microservice we call (synchronous REST communication).
 * Every client has a timeout so the Event service never hangs when another service is slow.
 */
@Configuration
public class RestClientConfig {

    @Value("${campusverse.services.timeout-ms:3000}")
    private long timeoutMs;

    private RestClient build(String baseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(timeoutMs));
        factory.setReadTimeout(Duration.ofMillis(timeoutMs));
        return RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
    }

    @Bean
    public RestClient campusSpacesRestClient(@Value("${campusverse.services.campus-spaces-url}") String url) {
        return build(url);
    }

    @Bean
    public RestClient reservationsRestClient(@Value("${campusverse.services.reservations-url}") String url) {
        return build(url);
    }

    @Bean
    public RestClient studentClubsRestClient(@Value("${campusverse.services.student-clubs-url}") String url) {
        return build(url);
    }
}

package com.example.OrderService.config;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    // create a bean of type webclient
    @Bean
    @LoadBalanced//this helps that even if the order-service finds multiple instance it won't be confused and calls all service one after another
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}


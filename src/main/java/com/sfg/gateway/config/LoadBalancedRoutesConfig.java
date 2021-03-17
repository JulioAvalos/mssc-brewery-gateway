package com.sfg.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("local-discovery")
@Configuration
public class LoadBalancedRoutesConfig {

    @Bean
    public RouteLocator loadBalancedRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/v1/beer*","/api/v1/beer/*", "/api/v1/beerUpc/*")
//                        .id("beer-service")
                        .uri("lb://beer-service"))
                .route(r -> r.path("/api/v1/customers/**")
//                        .id("order-service")
                        .uri("lb://order-service"))
                .route(r -> r.path("/api/v1/beer/*/inventory")
//                        .id("inventory-service")
                        .uri("lb://inventory-service"))
                .build();
    }
}

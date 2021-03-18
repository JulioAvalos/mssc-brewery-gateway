package com.sfg.gateway.config;

//import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
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
                .route(r -> r.path("/api/v1/beer*", "/api/v1/beer/*", "/api/v1/beerUpc/*")
//                        .id("beer-service")
                        .uri("lb://beer-service"))
                .route(r -> r.path("/api/v1/customers/**")
//                        .id("order-service")
                        .uri("lb://order-service"))
                .route(r -> r.path("/api/v1/beer/*/inventory")
//                        .id("inventory-service")
                        .filters(f -> f.circuitBreaker(c -> c.setName("inventoryCB")
                                .setFallbackUri("forward:/inventory-failover")
                                .setRouteId("inv-failover")))
                        .uri("lb://inventory-service"))
                .route(r -> r.path("/inventory-failover/**")
//                        .id("inventory-failover-service")
                        .uri("lb://inventory-failover"))
                .build();
    }


//    @Bean
//    public ReactiveResilience4JCircuitBreakerFactory factory() {
//        return new ReactiveResilience4JCircuitBreakerFactory();
//    }
}

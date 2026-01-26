package com.healthforu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("frontend", r -> r.path("/**")
                        .uri("http://localhost:5173"))

                .route("auth-service-v1", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.rewritePath("/api/v1/auth/(?<segment>.*)", "/api/${segment}"))// 헬스포유 웹사이트 들어갈때
                        .uri("http://localhost:8081"))

                .route("health-service-v1", r -> r.path("/api/v1/health/**")
                        .filters(f -> f.rewritePath("/api/v1/health/(?<segment>.*)", "/api/${segment}"))
                        .uri("http://localhost:8082"))
                .build();
    }

}

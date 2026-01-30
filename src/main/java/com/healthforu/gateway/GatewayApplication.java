package com.healthforu.gateway;

import com.healthforu.gateway.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;


@SpringBootApplication
@RequiredArgsConstructor
public class GatewayApplication {
    @Value("${ec2.public.ip}")
    private String ec2PublicIp;

    private final JwtFilter jwtFilter;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-public-service-v1", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.rewritePath("/api/v1/auth/(?<segment>.*)", "/api/auth/${segment}"))
                        .uri("http://auth-service:8081"))

                .route("user-private-service-v1", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.rewritePath("/api/v1/users/(?<segment>.*)", "/api/users/${segment}")
                                .filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("http://auth-service:8081"))

                .route("health-public-service-v1", r -> r.path("/api/v1/health/diseases/**", "/api/v1/health/recipes/**")
                        .filters(f -> f.rewritePath("/api/v1/health/(?<segment>.*)", "/api/${segment}"))
                        .uri("http://health-service:8082"))


                .route("health-private-service-v1", r -> r.path("/api/v1/health/favorites/**")
                        .filters(f -> f.rewritePath("/api/v1/health/(?<segment>.*)", "/api/${segment}")
                                .filter(jwtFilter.apply(new JwtFilter.Config())))
                        .uri("http://health-service:8082"))

                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 쿠키/인증 정보 허용
        config.setAllowedOrigins(List.of("http://" + ec2PublicIp));
        config.addAllowedOrigin("http://localhost:5173"); // 프론트 주소
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // GET, POST, PUT, DELETE 모두 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}

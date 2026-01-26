package com.healthforu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemogatewayApplication {
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
			.route("path_route", r -> r.path("/get")
				.uri("https://httpbin.org"))
			.route("host_route", r -> r.host("*.myhost.org")
				.uri("https://httpbin.org"))
			.route("rewrite_route", r -> r.host("*.rewrite.org")
				.filters(f -> f.rewritePath("/foo/(?<segment>.*)", "/${segment}"))
				.uri("https://httpbin.org"))
			.route("circuit_breaker_route", r -> r.host("*.circuitbreaker.org")
				.filters(f -> f.circuitBreaker(c -> c.setName("slowcmd")))
				.uri("https://httpbin.org"))
			.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemogatewayApplication.class, args);
	}

}
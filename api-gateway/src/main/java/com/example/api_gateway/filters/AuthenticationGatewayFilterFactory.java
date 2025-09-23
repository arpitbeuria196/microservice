package com.example.api_gateway.filters;

import com.example.api_gateway.service.JwtService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component // make it a Spring bean
@Slf4j
public class AuthenticationGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    // ✅ Only this constructor – Spring will inject JwtService
    public AuthenticationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!config.enabled) {
                return chain.filter(exchange);
            }

            String auth = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (auth == null || !auth.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = auth.substring(7).trim();
            Long userId;
            try {
                userId = jwtService.getUserIdFromToken(token);
            } catch (Exception e) {
                log.warn("JWT parse/verify failed", e);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // mutate request to add header for downstream services
            return chain.filter(
                    exchange.mutate()
                            .request(r -> r.headers(h -> h.add("X-User-Id", userId.toString())))
                            .build()
            );
        };
    }

    @Data
    public static class Config {
        private boolean enabled = true;
    }
}

package com.example.api_gateway.filters;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingOrdersFilter  extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {

    public LoggingOrdersFilter() {
    }


    @Override
    public GatewayFilter apply(NameConfig config) {
        return null;
    }
}

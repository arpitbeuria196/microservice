package com.example.inventory_service.configuration;


import feign.Capability;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class config {

    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }

    @Bean
    @LoadBalanced
    public RestClient restClient(RestClient.Builder builder)
    {
        return builder.build();
    }


    @Bean
    public  Capability capability(final MeterRegistry registry) {
        return new MicrometerCapability(registry);
    }
}

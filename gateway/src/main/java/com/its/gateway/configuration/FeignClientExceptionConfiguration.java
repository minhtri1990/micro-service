package com.its.gateway.configuration;

import com.its.gateway.model.exception.FeignClientException;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientExceptionConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> new FeignClientException(response.status(), "fail");
    }
}

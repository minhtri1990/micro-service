package com.its.module.configuration;

import com.its.module.model.exception.FeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;

public class FeignConfigurationAdapter {
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            String body = "{}";
            try {
                body = IOUtils.toString(response.body().asReader());
            } catch (Exception ignored) {}
            return new FeignClientException(response.status(), body);
        };
    }
}

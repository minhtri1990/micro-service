package com.smartcity.its.manager.violation.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.smartcity.its.manager.violation.model.request.scp.ScpRequest;

import feign.FeignException;
import feign.hystrix.FallbackFactory;

@FeignClient(name = "scp", url = "${SCP_SERVER}", fallbackFactory = ScpFeignClientFallback.class)
public interface ScpFeignClient {
    @PostMapping("/scp/start-iva")
    Object startService(@RequestBody ScpRequest scpRequest, @RequestHeader("apikey") String apikey);

    @PostMapping("/scp/stop-iva")
    Object stopService(@RequestBody ScpRequest scpRequest, @RequestHeader("apikey") String apikey);

    @PostMapping("/scp/stop-all")
    Object stopAllService(@RequestHeader("apikey") String apikey);
}

@Component
class ScpFeignClientFallback implements FallbackFactory<ScpFeignClient> {
    @Override
    public ScpFeignClient create(Throwable cause) {
        String httpStatus = cause instanceof FeignException ? Integer.toString(((FeignException) cause).status()) : "";
        System.out.println("fallback: " + httpStatus);
        return new ScpFeignClient() {
            @Override
            public Object startService(ScpRequest scpRequest, String apikey) {
                return httpStatus;
            }

            @Override
            public Object stopService(ScpRequest scpRequest, String apikey) {
                return httpStatus;
            }

            @Override
            public Object stopAllService(String apikey) {
                return httpStatus;
            }
        };
    }
}

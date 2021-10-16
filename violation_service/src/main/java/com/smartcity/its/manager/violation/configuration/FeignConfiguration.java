package com.smartcity.its.manager.violation.configuration;

import org.springframework.context.annotation.Configuration;

import com.its.module.configuration.FeignConfigurationAdapter;

/**
 * Cài đặt proxy cho feign client
 * Để thay đổi proxy hãy thay đổi env HOST_PROXY, PORT proxy
 * hoặc thay đổi code trong its_common_module
 * 
 * Để disable proxy chỉ cần xóa class này.
 */
//@Configuration
//public class FeignConfiguration extends FeignConfigurationAdapter {
//
//}

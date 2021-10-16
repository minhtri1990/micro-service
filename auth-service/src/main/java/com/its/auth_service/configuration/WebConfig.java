package com.its.auth_service.configuration;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.its.module.configuration.WebConfigAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.its.module.utils.Constants.Language;

@Configuration
public class WebConfig extends WebConfigAdapter {

}

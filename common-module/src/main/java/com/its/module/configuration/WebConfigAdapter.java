package com.its.module.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class WebConfigAdapter extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
//	List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("vi"));
//
//	@Override
//	public Locale getDefaultLocale() {
//		System.out.println("ok");
//		return new Locale("vi");
//	}

//	@Override
//	public Locale resolveLocale(HttpServletRequest request) {
//		String headerLang = request.getHeader("Accept-Language");
//		System.out.println(headerLang);
//		return headerLang == null || headerLang.isEmpty() ? DEFAULT
//				: Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
//	}


    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        System.out.println(request);
        return super.resolveLocale(request);
    }


    @Bean(name = "messageSource")
    public static MessageSource getMessageResource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    public static String translate(String message) {
        ReloadableResourceBundleMessageSource messageSource = (ReloadableResourceBundleMessageSource) getMessageResource();
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(message, null, locale);
    }
}


package com.its.auth_service.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private ServiceFilter serviceFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterBefore(serviceFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/roles/**").hasAnyAuthority("0")
                .antMatchers("/users/extract").hasAnyAuthority("0", "1")
                .antMatchers(HttpMethod.GET, "/users").hasAnyAuthority("0", "1")
                .antMatchers(HttpMethod.POST, "/users").hasAnyAuthority("0", "3")
                .antMatchers("/permissions/**").hasAnyAuthority("0", "6")
                .antMatchers("/roles/**").hasAnyAuthority("0","6")
                .antMatchers("/users/*/roles").hasAnyAuthority("0", "5")
                .antMatchers("/users/*/admin").hasAnyAuthority("0", "4")
                .antMatchers("/login").permitAll()
                .antMatchers("/token").permitAll()
                .antMatchers("/captcha/**").permitAll()
                .antMatchers("/users/forgot_password").permitAll()
                .antMatchers("/users/resend_forgot_password").permitAll()
                .antMatchers("/users/reset_password").permitAll()
                .anyRequest().authenticated();
    }
}

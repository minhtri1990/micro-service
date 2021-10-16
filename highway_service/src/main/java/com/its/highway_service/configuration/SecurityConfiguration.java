package com.its.highway_service.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private ServiceFilter serviceFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.addFilterBefore(serviceFilter, UsernamePasswordAuthenticationFilter.class);
		http.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/highways/**").authenticated()
				.antMatchers(HttpMethod.POST, "/highways").hasAnyAuthority("0", "16")
				.antMatchers(HttpMethod.PUT, "/highways/*").hasAnyAuthority("0", "16")
				.antMatchers(HttpMethod.DELETE, "/highways/*").hasAnyAuthority("0", "16")
				.antMatchers("/highways/supervisors").hasAnyAuthority("0","13")
				.anyRequest().denyAll();
	}
}
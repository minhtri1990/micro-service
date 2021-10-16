package com.its.auth_service.configuration;

import com.its.module.configuration.ServiceFilterAdapter;
import com.its.module.utils.Constants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ServiceFilter extends ServiceFilterAdapter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader(Constants.AuthHeader.USER_ID);
        if(userId != null && userId.length() > 0) {
            String role = request.getHeader(Constants.AuthHeader.ROLE);
            String permissions = request.getHeader(Constants.AuthHeader.PERMISSION);
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            if(permissions != null && permissions.length() > 0)
                grantedAuthorities = Arrays.stream(permissions.split(";")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(Integer.valueOf(userId), role, grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else SecurityContextHolder.clearContext();
        filterChain.doFilter(request, response);
    }
}

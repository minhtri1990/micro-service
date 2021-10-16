package com.its.module.configuration;

import com.its.module.utils.Constants;
import com.its.module.utils.Constants.AuthHeader;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ServiceFilterAdapter extends OncePerRequestFilter {
    private final String ROLE_PREFIX = "ROLE_";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader(AuthHeader.USER_ID);
        if(userId != null && userId.length() > 0) {
            String role = request.getHeader(AuthHeader.ROLE);
            String permissions = request.getHeader(AuthHeader.PERMISSION);
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            if(permissions != null && permissions.length() > 0)
                grantedAuthorities = Arrays.stream(permissions.split(";")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(Integer.valueOf(userId), role, grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else SecurityContextHolder.clearContext();
        filterChain.doFilter(request, response);
    }
}

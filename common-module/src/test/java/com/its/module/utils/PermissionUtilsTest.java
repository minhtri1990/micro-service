package com.its.module.utils;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PermissionUtilsTest {
    @Test
    void hasAnyAuthority() {
        Authentication authentication = Mockito.mock(Authentication.class);
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("0"));
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        boolean res = PermissionUtils.hasAnyAuthority(authentication, "0");
        MatcherAssert.assertThat(res, Matchers.equalTo(true));
    }

    @Test
    void hasAnyAuthority2() {
        Authentication authentication = Mockito.mock(Authentication.class);
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("0"));
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);
        boolean res = PermissionUtils.hasAnyAuthority(authentication, "1");
        MatcherAssert.assertThat(res, Matchers.equalTo(false));
    }
}
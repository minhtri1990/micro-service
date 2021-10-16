package com.its.module.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class PermissionUtils {
	public static boolean hasAnyAuthority(Authentication authentication, String... authorities) {
		if (authentication == null || authentication.getAuthorities() == null)
			return false;
		for (String authority : authorities)
			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(authority)))
				return true;
		return false;
	}

	public static boolean hasAnyRole(Authentication authentication, String... roles) {
		if (authentication == null || authentication.getAuthorities() == null)
			return false;
		for (String role : roles)
			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role)))
				return true;
		return false;
	}

}

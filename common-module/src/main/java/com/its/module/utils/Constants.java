package com.its.module.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Constants {

	public interface Language {
		List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("vi"));
		Locale DEFAULT = new Locale("vi");
	}

	public enum UserRole {
		SYSTEM_ADMIN, SUPERVISOR, POLICE
	}

	public interface Role {
		String ADMIN = "admin";
		String IOC = "ioc";
	}

	public enum RedisType {
		OTP_TOKEN, USER, CAPTCHA, OTP_RESET_PASSWORD
	}

	public interface Pagination {
		String PAGE_REQUEST_PARAM = "offset";
		String PAGE_SIZE_REQUEST_PARAM = "limit";
		String PAGE_DEFAULT = "1";
		String PAGE_SIZE_DEFAULT = "200";
		Integer MAX_PAGE_SIZE = 200;
		Integer ALL = 50000;
	}

	public interface AuthHeader {
		String AUTHORIZATION = "Authorization";
		String TOKEN_JWT = "token_jwt";
		String AUTH_APIKEY = "apikey";
		String OAUTH2_APIKEY = "api_key";
		String USER_ID = "user_id";
		String SSO_ID = "sso_id";
		String USERNAME = "username";
		String USER_INFO = "user_info";
		String ROLE = "role";
		String PERMISSION = "permission";
		String TNTID = "tntid";
		String IS_INTERNAL = "is_internal";
	}

	public interface Captcha {
		char[] CHARS = "ABCDEFGHJKLMNPQRSTUVWXYabcdefghjkmnpqrstuvwxy23456789".toCharArray();
		Integer LENGTH = 6;
		Integer USED_TIMES = 10;
	}

	public interface Validation {
		Set<String> TRIM = new HashSet<>(Arrays.asList("userName", "password", "code"));
		Set<String> TRIM_AND_LOWERCASE = new HashSet<>(Arrays.asList("email"));
		Set<String> REQUIRED_FIELDS = new HashSet<>(Arrays.asList("fullName", "phone", "email", "gender"));
	}

	public interface CacheName {
		String USER = "its-user-cache";
		String ROLE_PERMISSION = "its-role-permission-cache";
	}

	public interface FieldError {
		String NULL = "field.null";
		String MAX_LENGTH = "field.max_length";
		String RANGE_EXCEED = "field.range_exceed";
		String NOT_VALID = "field.not_valid";
	}

	public interface Regex {
		String PHONE = "^0([0-9]){8,11}$";
		String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	}
}

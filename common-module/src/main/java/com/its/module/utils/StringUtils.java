package com.its.module.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {
	private static final char[] CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz1234567890".toCharArray();
	private static final Random random = new Random();

	private static final char[] NUMBERS = "0123456789".toCharArray();
	private static final char[] LETTERS = "abcdefghijkmnpqrstuvwxyz".toCharArray();
	private static final char[] SPECIALS = "@#!$%^&".toCharArray();

	private static String getRandomChar(char[] charSet, int length) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i=0; i<length; i++) stringBuilder.append(charSet[random.nextInt(charSet.length)]);
		return stringBuilder.toString();
	}

	private static char getRandomChar(char[] charSet) {
		return charSet[random.nextInt(charSet.length)];
	}

	public static String getRandomPassword() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getRandomChar(SPECIALS, random.nextInt(2)+2));
		stringBuilder.append(getRandomChar(NUMBERS, random.nextInt(2)+2));
		stringBuilder.append(getRandomChar(LETTERS, random.nextInt(2)+2).toUpperCase());
		while (stringBuilder.length() < 12) stringBuilder.append(getRandomChar(LETTERS));
		return shuffle(stringBuilder.toString());
	}

	public static String shuffle(String input) {
		StringBuilder stringBuilder = new StringBuilder();
		List<Integer> indexes = new ArrayList<>();
		for(int i=0; i<input.length(); i++) indexes.add(i);
		Collections.shuffle(indexes);
		for(Integer i:indexes) stringBuilder.append(input.charAt(i));
		return stringBuilder.toString();
	}

	public static String getRandomString(int length) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			stringBuilder.append(getRandomChar(CHARS));
		}
		return stringBuilder.toString();
	}


	public static String getRandomNumberString(int length) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			stringBuilder.append(getRandomChar(NUMBERS));
		}
		return stringBuilder.toString();
	}

	public static String extractToken(String auth) {
		String[] arr = auth.split(" ");
		if (arr.length != 2 || !"Bearer".equals(arr[0]))
			return null;
		return arr[1];
	}

	public static String checkPasswordValidation(String password) {
		if (password == null || "".equals(password))
			return "password cannot be empty";
		if (password.length() > 20)
			return "password too long";
		return "";
	}

	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
			.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public static String checkEmailValidation(String email) {
		if (email == null || "".equals(email))
			return "Email cannot be empty";
		if (email.length() >= 255)
			return "Email too long";
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		if (!matcher.find())
			return "Email is invalid";
		return "";
	}

	public static boolean isValidEmail(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

	public static boolean isStrongPass(String pass) {
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		return pass.matches(regex);
	}

	private static final String PHONE_REGEX = "^0([0-9]){8,11}$";
	public static boolean isValidPhoneNumber(String phoneNumber) {
//		String regex = "^\\d{10}$";
		return phoneNumber.matches(PHONE_REGEX);
	}

	public static String strictlyTrim(String input) {
		if("".equals(input)) return "";
		StringBuilder stringBuilder = new StringBuilder(input.substring(0,1));
		char last = input.charAt(0);
		for(int i=1; i<input.length(); i++) {
			if(last == ' ' && input.charAt(i) == ' ') continue;
			stringBuilder.append(input.charAt(i));
			last = input.charAt(i);
		}
		return stringBuilder.toString().trim();
	}

	public static String concat(List<String> texts) {
		String buff = "";
		for (String string : texts) {
			if (org.apache.commons.lang3.StringUtils.isEmpty(buff)) {
				buff = buff + string;
			} else {
				buff = buff + "," + string;
			}
		}
		return buff;
	}

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
	public static String convertTimestamp(Long timestamp) {
		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Asia/Ho_Chi_Minh")).format(formatter);
	}
}

package com.viettel.parking.camera.utils;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

public class EncodeUtil {

	/**
	 * SHA256  encrypt
	 * @param text
	 * @return
	 */
	public static String sha256hex(String text) {
		String sha256hex = Hashing.sha256()
				.hashString(text, StandardCharsets.UTF_8)
				.toString();
		return sha256hex;
	}

}

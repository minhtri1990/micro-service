package com.viettel.parking.camera.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
	public static String parseToJsonAcceptNull(Object object) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.serializeNulls();
		return gsonBuilder.create().toJson(object);
	}

	public static String parseToJson(Object object) {
		return new Gson().toJson(object);
	}

	public static String toString(Object object) {
		return new Gson().toJson(object).toString();
	}

	public static <T> T parseJsonToObject(String json, Class<T> typeClass) {
		return new GsonBuilder().serializeNulls().create().fromJson(json, typeClass);
	}

	/**
	 * Process in case of List don't have include key
	 * 
	 * @param json
	 * @param typeClass
	 * @return
	 */
	public static <T> T parseJsonWithOutKeyToObject(String json, Class<T> typeClass) {
		/* Add key 'data' to Object value */
		return parseJsonToObject("{\"data\":" + json + "}", typeClass);
	}
}

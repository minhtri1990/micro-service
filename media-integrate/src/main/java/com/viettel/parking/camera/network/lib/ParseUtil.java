package com.viettel.parking.camera.network.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ParseUtil {
	private static ParseUtil utils = new ParseUtil();

	private static final Gson gson = new Gson();;

	private ParseUtil() {
	}

	public static ParseUtil getPaser() {
		return utils;
	}

	public String getString(Object object) {
		return gson.toJson(object);
	}

	public <T> T parseJsonToObject(String json, Class<T> typeClass) {
		return new GsonBuilder().serializeNulls().create().fromJson(json, typeClass);
	}
}

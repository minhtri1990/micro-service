package com.its.module.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

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

	public static <T> T mapObject(Object from, Class<T> clazz) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper.map(from, clazz);
	}

	public static <T> T mapObject(Object from, Type clazz) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper.map(from, clazz);
	}

	//aa
	public static String getStringFromStream(InputStream inputStream) throws IOException {
		InputStreamReader is = new InputStreamReader(inputStream);
		StringBuilder stringBuilder = new StringBuilder();
		char[] buffer = new char[1024];
		int len;
		while((len = is.read(buffer)) > 0) {
			stringBuilder.append(buffer, 0, len);
		}
		is.close();
		return stringBuilder.toString();
	}
}

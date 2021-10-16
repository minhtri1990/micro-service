package com.smartcity.its.manager.media.common;

import java.util.Date;
import java.util.UUID;

public class NameUtils {
	public static String randomUUIDName() {
		return UUID.randomUUID().toString() + "-" + new Date().getTime();
	}

	public static String appendSizeIntoFilename(String name, int height) {
		String[] temp = name.split("\\.");
		return temp[0] + "_" + height + "." + temp[1];
	}

	public static String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return fileName.substring(dotIndex + 1);
	}

	public static String getFileName(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return fileName.substring(0, dotIndex);
	}
}

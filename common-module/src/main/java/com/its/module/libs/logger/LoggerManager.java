package com.its.module.libs.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {

	/**
	 * get instance
	 * @param clazz
	 * @return
	 */
	public static LogUtils getLogger(final Class<?> clazz) {
		Logger logger = LogManager.getLogger(clazz);
		LogUtils log = LogUtils.builder()
				.logger(logger)
				.build();
		return log;
	}
}

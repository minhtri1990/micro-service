package com.its.module.libs.logger;

import org.apache.logging.log4j.Logger;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogUtils {

	private Logger logger;

	public void debug(Object message, Throwable t) {
		logger.debug(message, t);

	}

	public void debug(String message) {
		logger.debug(message);

	}

	public void debug(String message, Object... params) {
		logger.debug(message, params);

	}

	public void debug(String message, Throwable t) {
		logger.debug(message, t);

	}

	public void error(Object message, Throwable t) {
		logger.error(message, t);

	}

	public void error(String message) {
		logger.error(message);

	}

	public void error(String message, Object... params) {
		logger.error(message, params);

	}

	public void error(String message, Throwable t) {
		logger.error(message, t);

	}

	public void fatal(Object message, Throwable t) {
		logger.fatal(message, t);

	}

	public void fatal(String message) {
		logger.fatal(message);

	}

	public void fatal(String message, Object... params) {
		logger.fatal(message, params);

	}

	public void fatal(String message, Throwable t) {
		logger.fatal(message, t);

	}

	public void info(Object message, Throwable t) {
		logger.info(message, t);

	}

	public void info(String message) {
		logger.info(message);

	}

	public void info(String message, Object... params) {
		logger.info(message, params);

	}

	public void info(String message, Throwable t) {
		logger.info(message, t);

	}

	public void warn(Object message, Throwable t) {
		logger.warn(message, t);

	}

	public void warn(String message) {
		logger.warn(message);

	}

	public void warn(String message, Object... params) {
		logger.warn(message, params);

	}

	public void warn(String message, Throwable t) {
		logger.warn(message, t);

	}

}

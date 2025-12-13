package com.infy.wellness.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InfyLogger {

	private static Logger getLogger(Object source) {
		if (source instanceof Class<?>) {
			return LoggerFactory.getLogger((Class<?>) source);
		}
		return LoggerFactory.getLogger(source.getClass());
	}

	// -----------------------
	// INFO
	// -----------------------
	public static void info(Object source, String message) {
		getLogger(source).info(message);
	}

	public static void info(Object source, String message, Object... args) {
		getLogger(source).info(message, args);
	}

	// -----------------------
	// WARN
	// -----------------------
	public static void warn(Object source, String message) {
		getLogger(source).warn(message);
	}

	public static void warn(Object source, String message, Object... args) {
		getLogger(source).warn(message, args);
	}

	// -----------------------
	// DEBUG
	// -----------------------
	public static void debug(Object source, String message) {
		getLogger(source).debug(message);
	}

	public static void debug(Object source, String message, Object... args) {
		getLogger(source).debug(message, args);
	}

	// -----------------------
	// ERROR
	// -----------------------
	public static void error(Object source, String message) {
		getLogger(source).error(message);
	}

	public static void error(Object source, String message, Throwable exception) {
		getLogger(source).error(message, exception);
	}

	public static void error(Object source, String message, Throwable exception, Object... args) {
		getLogger(source).error(message, args, exception);
	}
}

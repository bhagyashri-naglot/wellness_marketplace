package com.infy.wellness.common.exceptions;

public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7702905133239138869L;

	public ResourceNotFoundException() {
		super("Resource not found");
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}
}

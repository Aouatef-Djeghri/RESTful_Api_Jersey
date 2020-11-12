package com.servicepro.exception;

public class EmptyParameterException extends RuntimeException  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1012176228356636814L;

	public EmptyParameterException(String message) {
		super(message);
	}
}

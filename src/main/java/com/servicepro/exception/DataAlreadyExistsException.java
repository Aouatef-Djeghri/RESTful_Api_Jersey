package com.servicepro.exception;

public class DataAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 4661708028900348166L;


	public DataAlreadyExistsException(String message) {
		super(message);
	}

}

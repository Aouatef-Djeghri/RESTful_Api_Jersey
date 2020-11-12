package com.servicepro.exception;

public class DatabaseException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2342712319773069366L;
	
	public DatabaseException(String message) {
		super(message);
	}
}

package com.zohocontacts.exception;

public class DBOperationException extends Exception {



	private static final long serialVersionUID = 1L;

	public DBOperationException() {
		super("Error Occured while Performing DB Operation");
	}

	public DBOperationException(String message) {
		super(message);
	}

	public DBOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBOperationException(Throwable cause) {
		super(cause);
	}

}

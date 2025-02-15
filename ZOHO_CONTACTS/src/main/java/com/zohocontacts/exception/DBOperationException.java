package com.zohocontacts.exception;

public class DBOperationException extends Exception {



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

package com.zohocontacts.exception;

public class InvalidDataException  extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidDataException() {
		super("Error Occured for Invalid Data");
	}

	public InvalidDataException(String message) {
		super(message);
	}

	public InvalidDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDataException(Throwable cause) {
		super(cause);
	}

}

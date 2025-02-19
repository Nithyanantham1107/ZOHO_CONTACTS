package com.zohocontacts.exception;

public class QueryBuilderException extends Exception {

	private static final long serialVersionUID = 1L;

	public QueryBuilderException() {
		super("Error Occured while Performing DB Operation");
	}

	public QueryBuilderException(String message) {
		super(message);
	}

	public QueryBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryBuilderException(Throwable cause) {
		super(cause);
	}
}

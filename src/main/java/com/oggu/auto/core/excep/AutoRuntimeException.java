package com.oggu.auto.core.excep;

public class AutoRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AutoRuntimeException(Throwable cause) {
		super(cause);
	}

	public AutoRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}

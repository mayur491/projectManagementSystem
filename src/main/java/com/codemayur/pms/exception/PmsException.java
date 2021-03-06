package com.codemayur.pms.exception;

public class PmsException extends Exception {

	private static final long serialVersionUID = 1L;

	public PmsException(
			String message,
			Throwable cause) {
		super(
				message,
				cause);
	}

	public PmsException(
			String message) {
		super(
				message);
	}
}

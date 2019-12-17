package br.com.accesstage.trustion.exception;

public class ForbiddenResponseException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ForbiddenResponseException(String message) {
		super(message);
	}
}

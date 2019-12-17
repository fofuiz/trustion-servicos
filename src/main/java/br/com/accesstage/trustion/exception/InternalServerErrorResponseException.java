package br.com.accesstage.trustion.exception;

public class InternalServerErrorResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InternalServerErrorResponseException(String mensagem) {
        super(mensagem);
    }
}

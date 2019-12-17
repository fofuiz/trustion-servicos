package br.com.accesstage.trustion.exception;

public class ConflictResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConflictResponseException(String mensagem) {
        super(mensagem);
    }
}

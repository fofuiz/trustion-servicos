package br.com.accesstage.trustion.exception;

public class UsuarioNaoExisteException extends RuntimeException {

    public UsuarioNaoExisteException(String message) {
        super(message);
    }
}

package br.com.accesstage.trustion.controller.advice;

import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.ResourceNotFoundException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.exception.ResponseException;
import br.com.accesstage.trustion.exception.ConflictResponseException;
import br.com.accesstage.trustion.exception.UnauthorizedException;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdviceResponseGeneric extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {ConflictResponseException.class})
	protected ResponseEntity<ResponseException> conflict(ConflictResponseException ex) {

		ResponseException responseException = new ResponseException();
		responseException.setCodigo(HttpStatus.CONFLICT.value());
		responseException.setMensagem(ex.getMessage());

		return new ResponseEntity<ResponseException>(responseException, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(value = {InternalServerErrorResponseException.class})
	protected ResponseEntity<ResponseException> conflict(InternalServerErrorResponseException ex) {

		ResponseException responseException = new ResponseException();
		responseException.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.value());
		responseException.setMensagem(ex.getMessage());

		return new ResponseEntity<ResponseException>(responseException, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = {BadRequestResponseException.class})
	protected ResponseEntity<ResponseException> conflict(BadRequestResponseException ex) {

		ResponseException responseException = new ResponseException();
		responseException.setCodigo(HttpStatus.BAD_REQUEST.value());
		responseException.setMensagem(ex.getMessage());

		return new ResponseEntity<ResponseException>(responseException, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {ForbiddenResponseException.class})
	protected ResponseEntity<ResponseException> forbidden(ForbiddenResponseException ex){
		ResponseException responseException = new ResponseException();
		responseException.setCodigo(HttpStatus.FORBIDDEN.value());
		responseException.setMensagem(ex.getMessage());
		return new ResponseEntity<>(responseException, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(value = {UnauthorizedException.class})
	protected ResponseEntity<ResponseException> forbidden(UnauthorizedException ex){
		ResponseException responseException = new ResponseException();
		responseException.setCodigo(HttpStatus.UNAUTHORIZED.value());
		responseException.setMensagem(ex.getMessage());
		return new ResponseEntity<>(responseException, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(value = {ResourceNotFoundException.class})
	protected ResponseEntity<ResponseException> resourceNotFound(ResourceNotFoundException ex){
		ResponseException responseException = new ResponseException();
		responseException.setCodigo(HttpStatus.NOT_FOUND.value());
		responseException.setMensagem(ex.getMessage());
		return new ResponseEntity<>(responseException, HttpStatus.NOT_FOUND);
	}
}
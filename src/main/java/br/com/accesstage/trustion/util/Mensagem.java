package br.com.accesstage.trustion.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class Mensagem {

    private static MessageSource mensagem;
    private static String mensagemRetorno;

    @Autowired
	private MessageSource messageSource;

    @PostConstruct
    public void init() {
    	mensagem = messageSource;    	
    }
    
	public static String get(String code) {

		try {
			mensagemRetorno = mensagem.getMessage(code, null, null);

		} catch (NoSuchMessageException e) {
			mensagemRetorno = code;
		}

		return mensagemRetorno;
	}

	public static String get(String code, Object[] args) {

		try {
			mensagemRetorno = mensagem.getMessage(code, args, null);

		} catch (Exception e) {
			mensagemRetorno = code;
		}

		return mensagemRetorno;
	}

	public static String get(BindingResult camposInvalidos) {

		try {
			FieldError fieldError = (FieldError) camposInvalidos.getAllErrors().get(0);
			//mensagemRetorno =  fieldError.getField() + " " + fieldError.getDefaultMessage();
			if(fieldError.getDefaultMessage().startsWith("msg")) {
				mensagemRetorno = Mensagem.get(fieldError.getDefaultMessage());
			}else {
				mensagemRetorno = fieldError.getDefaultMessage();
			}
		} catch (Exception e) {
			mensagemRetorno = camposInvalidos.toString();
		}

		return mensagemRetorno;
	}
}

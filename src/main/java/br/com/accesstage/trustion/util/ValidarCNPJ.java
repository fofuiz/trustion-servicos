package br.com.accesstage.trustion.util;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.InvalidStateException;

public class ValidarCNPJ {
	
	private static CNPJValidator validator = null;
	
	private static void newInstance() {
		if(validator == null) {
			synchronized (ValidarCNPJ.class){
				if(validator == null){
					validator = new CNPJValidator();
				}
			}
		}
	}
	
	public static void validarCNPJ(String cnpj) throws InvalidStateException{
		newInstance();
		validator.assertValid(cnpj);
	}
}

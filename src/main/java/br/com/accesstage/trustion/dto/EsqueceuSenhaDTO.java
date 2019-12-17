package br.com.accesstage.trustion.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class EsqueceuSenhaDTO {
	
	@NotNull
	@NotEmpty
	@NotBlank
	private String login;
	
	private String email;
	

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}

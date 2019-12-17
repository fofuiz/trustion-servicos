package br.com.accesstage.trustion.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class RedefinicaoSenhaDTO {

	@NotNull(message="Favor, informe a senha atual.")
	@NotEmpty(message="Favor, informe a senha atual.")
	@NotBlank(message="Favor, informe a senha atual.")
	private String senhaAtual;
	
	@NotNull(message="Favor, informe a nova senha.")
	@NotEmpty(message="Favor, informe a nova senha.")
	@NotBlank(message="Favor, informe a nova senha.")
	private String novaSenha;
	
	@NotNull(message="Favor, confirme a nova senha.")
	@NotEmpty(message="Favor, confirme a nova senha.")
	@NotBlank(message="Favor, confirme a nova senha.")
	private String confirmaNovaSenha;

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}
	
	public String getNovaSenha() {
		return novaSenha;
	}
	
	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
	
	public String getConfirmaNovaSenha() {
		return confirmaNovaSenha;
	}
	
	public void setConfirmaNovaSenha(String confirmaNovaSenha) {
		this.confirmaNovaSenha = confirmaNovaSenha;
	}
}

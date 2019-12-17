package br.com.accesstage.trustion.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class TipoServicoDTO {
	
	private Long idTipoServico;
	
	@NotEmpty
	@NotBlank
	private String nomeServico;
	
	@NotNull
	private Long idGrupoEconomico;
	
	@NotEmpty
	@NotBlank
	private String descServico;
	
	private String nomeGrupoEconomicoTransportadora;
	
	private Date dataCriacao;

	private Long idUsuarioCriacao;

	private Date dataAlteracao;

	private Long idUsuarioAlteracao;

	
	public Long getIdTipoServico() {
		return idTipoServico;
	}

	public String getNomeServico() {
		return nomeServico;
	}

	public Long getIdGrupoEconomico() {
		return idGrupoEconomico;
	}

	public String getDescServico() {
		return descServico;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public Long getIdUsuarioCriacao() {
		return idUsuarioCriacao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public Long getIdUsuarioAlteracao() {
		return idUsuarioAlteracao;
	}

	public void setIdTipoServico(Long idTipoServico) {
		this.idTipoServico = idTipoServico;
	}

	public void setNomeServico(String nomeServico) {
		this.nomeServico = nomeServico;
	}

	public void setIdGrupoEconomico(Long idGrupoEconomico) {
		this.idGrupoEconomico = idGrupoEconomico;
	}

	public void setDescServico(String descServico) {
		this.descServico = descServico;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public void setIdUsuarioCriacao(Long idUsuarioCriacao) {
		this.idUsuarioCriacao = idUsuarioCriacao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public void setIdUsuarioAlteracao(Long idUsuarioAlteracao) {
		this.idUsuarioAlteracao = idUsuarioAlteracao;
	}

	public String getNomeGrupoEconomicoTransportadora() {
		return nomeGrupoEconomicoTransportadora;
	}

	public void setNomeGrupoEconomicoTransportadora(String nomeGrupoEconomicoTransportadora) {
		this.nomeGrupoEconomicoTransportadora = nomeGrupoEconomicoTransportadora;
	}
	
}

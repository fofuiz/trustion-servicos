package br.com.accesstage.trustion.dto;

import java.util.Date;

public class TipoCreditoDTO {
	
	private Long idTipoCredito;
	
	private String descricao;
	
	private Date dataCriacao;

	private Long idUsuarioCriacao;

	private Date dataAlteracao;

	private Long idUsuarioAlteracao;

	public TipoCreditoDTO() {
		super();
	}

	public Long getIdTipoCredito() {
		return idTipoCredito;
	}

	public void setIdTipoCredito(Long idTipoCredito) {
		this.idTipoCredito = idTipoCredito;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Long getIdUsuarioCriacao() {
		return idUsuarioCriacao;
	}

	public void setIdUsuarioCriacao(Long idUsuarioCriacao) {
		this.idUsuarioCriacao = idUsuarioCriacao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Long getIdUsuarioAlteracao() {
		return idUsuarioAlteracao;
	}

	public void setIdUsuarioAlteracao(Long idUsuarioAlteracao) {
		this.idUsuarioAlteracao = idUsuarioAlteracao;
	}
}

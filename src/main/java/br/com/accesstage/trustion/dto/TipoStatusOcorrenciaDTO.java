package br.com.accesstage.trustion.dto;

import java.util.Date;

public class TipoStatusOcorrenciaDTO {
	
	private Long idTipoStatusOcorrencia;
	
	private String descricao;

	private Date dataCriacao;

	private Long idUsuarioCriacao;
	
	private Date dataAlteracao;
	
	private Long idUsuarioAlteracao;
	

	public TipoStatusOcorrenciaDTO() {
		super();
	}

	public Long getIdTipoStatusOcorrencia() {
		return idTipoStatusOcorrencia;
	}

	public void setIdTipoStatusOcorrencia(Long idTipoStatusOcorrencia) {
		this.idTipoStatusOcorrencia = idTipoStatusOcorrencia;
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

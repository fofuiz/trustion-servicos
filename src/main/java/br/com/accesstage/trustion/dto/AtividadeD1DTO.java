package br.com.accesstage.trustion.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class AtividadeD1DTO {

	private Long idAtividade;
	@NotNull
	private Long idOcorrencia;
	private Long idUsuario;
	private String usuario;
	private Date dataHorario;
	@NotNull
	private String atividade;
	@NotNull
	private String tipoAtividade;
	private Date dataCriacao;
	private Long idUsuarioCriacao;
	private Date dataAlteracao;
	private Long idUsuarioAlteracao;
	private String responsavel;
	private String statusOcorrencia;
	private Long idTipoOcorrencia;
	private String motivoDescricao;
	private Long idMotivo;
	private boolean atividadeMescla;
	private boolean habilitado;

	public AtividadeD1DTO() {
		super();
	}

	public Long getIdAtividade() {
		return idAtividade;
	}

	public void setIdAtividade(Long idAtividade) {
		this.idAtividade = idAtividade;
	}

	public Long getIdOcorrencia() {
		return idOcorrencia;
	}

	public void setIdOcorrencia(Long idOcorrencia) {
		this.idOcorrencia = idOcorrencia;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getDataHorario() {
		return dataHorario;
	}

	public void setDataHorario(Date dataHorario) {
		this.dataHorario = dataHorario;
	}

	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public String getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(String tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
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

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getStatusOcorrencia() {
		return statusOcorrencia;
	}

	public void setStatusOcorrencia(String statusOcorrencia) {
		this.statusOcorrencia = statusOcorrencia;
	}

	public Long getIdTipoOcorrencia() {
		return idTipoOcorrencia;
	}

	public void setIdTipoOcorrencia(Long idTipoOcorrencia) {
		this.idTipoOcorrencia = idTipoOcorrencia;
	}

	public String getMotivoDescricao() {
		return motivoDescricao;
	}

	public void setMotivoDescricao(String motivoDescricao) {
		this.motivoDescricao = motivoDescricao;
	}

	public Long getIdMotivo() {
		return idMotivo;
	}

	public void setIdMotivo(Long idMotivo) {
		this.idMotivo = idMotivo;
	}

	public boolean isAtividadeMescla() {
		return atividadeMescla;
	}

	public void setAtividadeMescla(boolean atividadeMescla) {
		this.atividadeMescla = atividadeMescla;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
}

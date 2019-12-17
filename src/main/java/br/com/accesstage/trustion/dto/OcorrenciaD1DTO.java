package br.com.accesstage.trustion.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.accesstage.trustion.model.Ocorrencia;


public class OcorrenciaD1DTO {
	
	private Long idRelatorioAnalitico;
	private Long idOcorrencia;
	private String grupoEconomico;
	private String empresa;
	private String modeloNegocio;
	
	private Date dataConferencia;
	private BigDecimal valorConferido;
	private Date dataCredito;
	private BigDecimal valorCreditado;
	private BigDecimal valorDeclarado;
	public BigDecimal getValorColeta() {
		return valorColeta;
	}


	public void setValorColeta(BigDecimal valorColeta) {
		this.valorColeta = valorColeta;
	}


	private BigDecimal valorColeta;
	
	@NotNull
	private Long idTipoQuestionamento;
	
	private String tipoQuestionamento;
	
	@NotNull
	@NotEmpty
	@NotBlank
	private String observacao;
	
	@NotNull
	private BigDecimal valorQuestionado;
	
	private Long idTipoStatusOcorrencia;
	private String tipoStatusOcorrencia;
	private Date dataStatusOcorrencia;
	private String statusConciliacao;
	private Long idUsuarioCriacao;
	private Date dataCriacao;
	private Long idUsuarioAlteracao;
	private Date dataAlteracao;
	private Boolean exibirArqResOcorrencia;
	private String arquivoResumoOcorrencia;
	
	private List<Ocorrencia> mesclas;
	
	private Boolean concluido;
	
	private Boolean isOcorrenciaSelected;
	
	private BigDecimal valorAjuste;
	
	private Long idOcorrenciaMescla;
	
	
	public OcorrenciaD1DTO() {
		super();
	}


	public Long getIdRelatorioAnalitico() {
		return idRelatorioAnalitico;
	}


	public void setIdRelatorioAnalitico(Long idRelatorioAnalitico) {
		this.idRelatorioAnalitico = idRelatorioAnalitico;
	}


	public Long getIdOcorrencia() {
		return idOcorrencia;
	}


	public void setIdOcorrencia(Long idOcorrencia) {
		this.idOcorrencia = idOcorrencia;
	}


	public String getGrupoEconomico() {
		return grupoEconomico;
	}


	public void setGrupoEconomico(String grupoEconomico) {
		this.grupoEconomico = grupoEconomico;
	}


	public String getEmpresa() {
		return empresa;
	}


	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}


	public String getModeloNegocio() {
		return modeloNegocio;
	}


	public void setModeloNegocio(String modeloNegocio) {
		this.modeloNegocio = modeloNegocio;
	}


	public Long getIdTipoQuestionamento() {
		return idTipoQuestionamento;
	}


	public void setIdTipoQuestionamento(Long idTipoQuestionamento) {
		this.idTipoQuestionamento = idTipoQuestionamento;
	}


	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public BigDecimal getValorQuestionado() {
		return valorQuestionado;
	}


	public void setValorQuestionado(BigDecimal valorQuestionado) {
		this.valorQuestionado = valorQuestionado;
	}


	public Long getIdTipoStatusOcorrencia() {
		return idTipoStatusOcorrencia;
	}


	public void setIdTipoStatusOcorrencia(Long idTipoStatusOcorrencia) {
		this.idTipoStatusOcorrencia = idTipoStatusOcorrencia;
	}


	public Date getDataStatusOcorrencia() {
		return dataStatusOcorrencia;
	}


	public void setDataStatusOcorrencia(Date dataStatusOcorrencia) {
		this.dataStatusOcorrencia = dataStatusOcorrencia;
	}

	public Long getIdUsuarioCriacao() {
		return idUsuarioCriacao;
	}


	public void setIdUsuarioCriacao(Long idUsuarioCriacao) {
		this.idUsuarioCriacao = idUsuarioCriacao;
	}


	public Date getDataCriacao() {
		return dataCriacao;
	}


	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}


	public Long getIdUsuarioAlteracao() {
		return idUsuarioAlteracao;
	}


	public void setIdUsuarioAlteracao(Long idUsuarioAlteracao) {
		this.idUsuarioAlteracao = idUsuarioAlteracao;
	}


	public Date getDataAlteracao() {
		return dataAlteracao;
	}


	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}


	public Date getDataConferencia() {
		return dataConferencia;
	}


	public void setDataConferencia(Date dataConferencia) {
		this.dataConferencia = dataConferencia;
	}


	public BigDecimal getValorConferido() {
		return valorConferido;
	}


	public void setValorConferido(BigDecimal valorConferido) {
		this.valorConferido = valorConferido;
	}


	public Date getDataCredito() {
		return dataCredito;
	}


	public void setDataCredito(Date dataCredito) {
		this.dataCredito = dataCredito;
	}


	public BigDecimal getValorCreditado() {
		return valorCreditado;
	}


	public BigDecimal getValorDeclarado() {
		return valorDeclarado;
	}


	public void setValorDeclarado(BigDecimal valorDeclarado) {
		this.valorDeclarado = valorDeclarado;
	}


	public void setValorCreditado(BigDecimal valorCreditado) {
		this.valorCreditado = valorCreditado;
	}


	public String getTipoQuestionamento() {
		return tipoQuestionamento;
	}


	public void setTipoQuestionamento(String tipoQuestionamento) {
		this.tipoQuestionamento = tipoQuestionamento;
	}


	public String getTipoStatusOcorrencia() {
		return tipoStatusOcorrencia;
	}


	public void setTipoStatusOcorrencia(String tipoStatusOcorrencia) {
		this.tipoStatusOcorrencia = tipoStatusOcorrencia;
	}


	public Boolean getExibirArqResOcorrencia() {
		return exibirArqResOcorrencia;
	}


	public void setExibirArqResOcorrencia(Boolean exibirArqResOcorrencia) {
		this.exibirArqResOcorrencia = exibirArqResOcorrencia;
	}


	public String getArquivoResumoOcorrencia() {
		return arquivoResumoOcorrencia;
	}


	public void setArquivoResumoOcorrencia(String arquivoResumoOcorrencia) {
		this.arquivoResumoOcorrencia = arquivoResumoOcorrencia;
	}


	public String getStatusConciliacao() {
		return statusConciliacao;
	}


	public void setStatusConciliacao(String statusConciliacao) {
		this.statusConciliacao = statusConciliacao;
	}

	public Boolean getConcluido() {
		return concluido;
	}

	public void setConcluido(Boolean concluido) {
		this.concluido = concluido;
	}


	public List<Ocorrencia> getMesclas() {
		return mesclas;
	}


	public void setMesclas(List<Ocorrencia> mesclas) {
		this.mesclas = mesclas;
	}


	public Boolean getIsOcorrenciaSelected() {
		return isOcorrenciaSelected;
	}


	public void setIsOcorrenciaSelected(Boolean isOcorrenciaSelected) {
		this.isOcorrenciaSelected = isOcorrenciaSelected;
	}


	public BigDecimal getValorAjuste() {
		return valorAjuste;
	}


	public void setValorAjuste(BigDecimal valorAjuste) {
		this.valorAjuste = valorAjuste;
	}


	public Long getIdOcorrenciaMescla() {
		return idOcorrenciaMescla;
	}


	public void setIdOcorrenciaMescla(Long idOcorrenciaMescla) {
		this.idOcorrenciaMescla = idOcorrenciaMescla;
	}
}

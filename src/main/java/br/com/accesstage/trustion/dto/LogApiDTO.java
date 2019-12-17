package br.com.accesstage.trustion.dto;

import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

public class LogApiDTO {
	
private Long IdLogAPI;
	
	private Long idGrupoEconomico;
	
	private String grupoEconomico;
	
	private Long idEmpresa;
	
	private String empresa;
	
	private Long idCofre;
	
	private String numSerialCofre;
	
	private String api;
	
	private String statusConsulta;

	private Date dataLog;
	
	private String mensagem;
	
	@NotNull
	private Date dataInicial;
	
	@NotNull
	private Date dataFinal;
	
	private Date dataCriacao;
	
	private Date dataAlteracao;

	public LogApiDTO() {
		super();
	}

	public Long getIdLogAPI() {
		return IdLogAPI;
	}

	public void setIdLogAPI(Long idLogAPI) {
		IdLogAPI = idLogAPI;
	}

	public Long getIdGrupoEconomico() {
		return idGrupoEconomico;
	}

	public void setIdGrupoEconomico(Long idGrupoEconomico) {
		this.idGrupoEconomico = idGrupoEconomico;
	}

	public String getGrupoEconomico() {
		return grupoEconomico;
	}

	public void setGrupoEconomico(String grupoEconomico) {
		this.grupoEconomico = grupoEconomico;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public Long getIdCofre() {
		return idCofre;
	}

	public void setIdCofre(Long idCofre) {
		this.idCofre = idCofre;
	}

	public String getNumSerialCofre() {
		return numSerialCofre;
	}

	public void setNumSerialCofre(String numSerialCofre) {
		this.numSerialCofre = numSerialCofre;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getStatusConsulta() {
		return statusConsulta;
	}

	public void setStatusConsulta(String statusConsulta) {
		this.statusConsulta = statusConsulta;
	}

	public Date getDataLog() {
		return dataLog;
	}

	public void setDataLog(Date dataLog) {
		this.dataLog = dataLog;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	
	@AssertTrue(message = "intervalo entre as datas \u00E9 inv\u00E1lido.")
	public boolean isRangeValid() {
		if(dataFinal != null && dataInicial != null) {
			return dataFinal.compareTo(dataInicial) >= 0;
		}else {
			return true;
		}
		
	}
}

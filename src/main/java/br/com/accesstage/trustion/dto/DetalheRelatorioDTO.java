package br.com.accesstage.trustion.dto;

import java.math.BigDecimal;
import java.util.Date;

public class DetalheRelatorioDTO {
	
	private Long idDetalheRelatorio;
	private String numSerie;
	private Long idEquipamento;
	private Date depositoDT;
	private Long codigoMovimento;
	private String depositante;
	private String  tipoDeposito;
	private Long codigoFechamento;
	private Long sequencia;
	private BigDecimal valorTotal;
	private BigDecimal totalDeclarado;
	private Date dataCriacao;
	private Date dataColeta;

	public Long getIdDetalheRelatorio() {
		return idDetalheRelatorio;
	}
	
	public void setIdDetalheRelatorio(Long idDetalheRelatorio) {
		this.idDetalheRelatorio = idDetalheRelatorio;
	}
	
	public String getNumSerie() {
		return numSerie;
	}
	
	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}
	
	public Long getCodigoMovimento() {
		return codigoMovimento;
	}
	
	public void setCodigoMovimento(Long codigoMovimento) {
		this.codigoMovimento = codigoMovimento;
	}
	
	public String getDepositante() {
		return depositante;
	}
	
	public void setDepositante(String depositante) {
		this.depositante = depositante;
	}
	
	public String getTipoDeposito() {
		return tipoDeposito;
	}
	
	public void setTipoDeposito(String tipoDeposito) {
		this.tipoDeposito = tipoDeposito;
	}
	
	public Long getCodigoFechamento() {
		return codigoFechamento;
	}
	
	public void setCodigoFechamento(Long codigoFechamento) {
		this.codigoFechamento = codigoFechamento;
	}
	
	public Long getSequencia() {
		return sequencia;
	}
	
	public void setSequencia(Long sequencia) {
		this.sequencia = sequencia;
	}
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getTotalDeclarado() {
		return totalDeclarado;
	}

	public void setTotalDeclarado(BigDecimal totalDeclarado) {
		this.totalDeclarado = totalDeclarado;
	}

	public Long getIdEquipamento() {
		return idEquipamento;
	}

	public void setIdEquipamento(Long idEquipamento) {
		this.idEquipamento = idEquipamento;
	}

	public Date getDepositoDT() {
		return depositoDT;
	}

	public void setDepositoDT(Date depositoDT) {
		this.depositoDT = depositoDT;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataColeta() {
		return dataColeta;
	}

	public void setDataColeta(Date dataColeta) {
		this.dataColeta = dataColeta;
	}
	
}

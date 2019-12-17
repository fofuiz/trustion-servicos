package br.com.accesstage.trustion.dto;

import java.util.Date;

public class ConciliadoDTO {

    private Long idConciliado;

    private String cnpj;

    private Date dataLancamento;

    private Date dataConciliacao;

    private Date dataCriacao;

    private Long idUsuarioCriacao;

    private Date dataAlteracao;

    private Long idUsuarioAlteracao;

    private String tipoRelatorio;

    public Long getIdConciliado() {
        return idConciliado;
    }

    public void setIdConciliado(Long idConciliado) {
        this.idConciliado = idConciliado;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Date getDataConciliacao() {
        return dataConciliacao;
    }

    public void setDataConciliacao(Date dataConciliacao) {
        this.dataConciliacao = dataConciliacao;
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

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

	@Override
	public String toString() {
		return "ConciliadoDTO [idConciliado=" + idConciliado + ", cnpj=" + cnpj + ", dataLancamento=" + dataLancamento
				+ ", dataConciliacao=" + dataConciliacao + ", dataCriacao=" + dataCriacao + ", idUsuarioCriacao="
				+ idUsuarioCriacao + ", dataAlteracao=" + dataAlteracao + ", idUsuarioAlteracao=" + idUsuarioAlteracao
				+ ", tipoRelatorio=" + tipoRelatorio + "]";
	}
    
}

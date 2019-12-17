package br.com.accesstage.trustion.dto;

import java.util.Date;

public class DadosBancariosDTO {

    private Long idDadosBancarios;

    private Long conta;

    private Long agencia;

    private Long dv;

    private Long idBanco;

    private Long idEmpresa;

    private String descricao;

    private Long idUsuario;

    private Long idUsuarioAlteracao;

    private Date dataCriacao;

    private Date dataAlteracao;

    public DadosBancariosDTO() {

    }

    public DadosBancariosDTO(Long conta, Long agencia, Long idBanco, Long dv) {
        this.conta = conta;
        this.agencia = agencia;
        this.idBanco = idBanco;
        this.dv = dv;
    }

    public Long getIdDadosBancarios() {
        return idDadosBancarios;
    }

    public void setIdDadosBancarios(Long idDadosBancarios) {
        this.idDadosBancarios = idDadosBancarios;
    }

    public Long getConta() {
        return conta;
    }

    public void setConta(Long conta) {
        this.conta = conta;
    }

    public Long getAgencia() {
        return agencia;
    }

    public void setAgencia(Long agencia) {
        this.agencia = agencia;
    }

    public Long getDv() {
        return dv;
    }

    public void setDv(Long dv) {
        this.dv = dv;
    }

    public Long getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(Long idBanco) {
        this.idBanco = idBanco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdUsuarioAlteracao() {
        return idUsuarioAlteracao;
    }

    public void setIdUsuarioAlteracao(Long idUsuarioAlteracao) {
        this.idUsuarioAlteracao = idUsuarioAlteracao;
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
}

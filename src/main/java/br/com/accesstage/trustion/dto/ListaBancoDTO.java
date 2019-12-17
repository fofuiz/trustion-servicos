package br.com.accesstage.trustion.dto;

import java.util.Date;

public class ListaBancoDTO {

    private Long idListaBanco;

    private String codigoBanco;

    private String descricao;

    private Date dataCriacao;

    private Long idUsuarioCriacao;

    private Date dataAlteracao;

    private Long idUsuarioAlteracao;

    public ListaBancoDTO() {
    }

    public Long getIdListaBanco() {
        return idListaBanco;
    }

    public void setIdListaBanco(Long idListaBanco) {
        this.idListaBanco = idListaBanco;
    }

    public String getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
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

package br.com.accesstage.trustion.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ExtratoElegivelDTO {

    private Long idExtElegivel;

    private Integer codigoBanco;

    private String banco;

    private Integer conta;

    private String cnpj;

    private Integer agencia;

    private Date dataLancamento;

    private BigDecimal valorLancamento;

    private String historicoLancamento;

    private Long idConciliacao;

    public Long getIdExtElegivel() {
        return idExtElegivel;
    }

    public void setIdExtElegivel(Long idExtElegivel) {
        this.idExtElegivel = idExtElegivel;
    }

    public Integer getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(Integer codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Integer getConta() {
        return conta;
    }

    public void setConta(Integer conta) {
        this.conta = conta;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Integer getAgencia() {
        return agencia;
    }

    public void setAgencia(Integer agencia) {
        this.agencia = agencia;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public BigDecimal getValorLancamento() {
        return valorLancamento;
    }

    public void setValorLancamento(BigDecimal valorLancamento) {
        this.valorLancamento = valorLancamento;
    }

    public String getHistoricoLancamento() {
        return historicoLancamento;
    }

    public void setHistoricoLancamento(String historicoLancamento) {
        this.historicoLancamento = historicoLancamento;
    }

    public Long getIdConciliacao() {
        return idConciliacao;
    }

    public void setIdConciliacao(Long idConciliacao) {
        this.idConciliacao = idConciliacao;
    }
}

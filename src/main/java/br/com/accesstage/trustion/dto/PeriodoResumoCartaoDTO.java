package br.com.accesstage.trustion.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

public class PeriodoResumoCartaoDTO {

    private Long idPeriodoResumo;
    private Long idUsuario;
    @Max(value = 60, message = "msg.intervalo.periodo")
    @Min(value = 0, message = "msg.intervalo.periodo")
    private Long periodoVenda;
    @Max(value = 60, message = "msg.intervalo.periodo")
    @Min(value = 0, message = "msg.intervalo.periodo")
    private Long periodoRecebimento;
    @Max(value = 60, message = "msg.intervalo.periodo")
    @Min(value = 0, message = "msg.intervalo.periodo")
    private Long periodoRecebimentoFuturo;
    private Date dataCriacao;
    private Long idUsuarioCriacao;
    private Date dataAlteracao;
    private Long idUsuarioAlteracao;

    public PeriodoResumoCartaoDTO() {
    }

    public Long getIdPeriodoResumo() {
        return idPeriodoResumo;
    }

    public void setIdPeriodoResumo(Long idPeriodoResumo) {
        this.idPeriodoResumo = idPeriodoResumo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getPeriodoVenda() {
        return periodoVenda;
    }

    public void setPeriodoVenda(Long periodoVenda) {
        this.periodoVenda = periodoVenda;
    }

    public Long getPeriodoRecebimento() {
        return periodoRecebimento;
    }

    public void setPeriodoRecebimento(Long periodoRecebimento) {
        this.periodoRecebimento = periodoRecebimento;
    }

    public Long getPeriodoRecebimentoFuturo() {
        return periodoRecebimentoFuturo;
    }

    public void setPeriodoRecebimentoFuturo(Long periodoRecebimentoFuturo) {
        this.periodoRecebimentoFuturo = periodoRecebimentoFuturo;
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

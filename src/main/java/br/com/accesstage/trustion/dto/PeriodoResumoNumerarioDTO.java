package br.com.accesstage.trustion.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

public class PeriodoResumoNumerarioDTO {

    private Long idPeriodoResumo;

    private Long idUsuario;

    @Max(value = 60, message = "msg.intervalo.periodo")
    @Min(value = 0, message = "msg.intervalo.periodo")
    private Long periodoColeta;

    @Max(value = 60, message = "msg.intervalo.periodo")
    @Min(value = 0, message = "msg.intervalo.periodo")
    private Long periodoConferencia;

    @Max(value = 60, message = "msg.intervalo.periodo")
    @Min(value = 0, message = "msg.intervalo.periodo")
    private Long periodoCredito;

    private Date dataCriacao;

    private Long idUsuarioCriacao;

    private Date dataAlteracao;

    private Long idUsuarioAlteracao;

    public PeriodoResumoNumerarioDTO() {
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

    public Long getPeriodoColeta() {
        return periodoColeta;
    }

    public void setPeriodoColeta(Long periodoColeta) {
        this.periodoColeta = periodoColeta;
    }

    public Long getPeriodoConferencia() {
        return periodoConferencia;
    }

    public void setPeriodoConferencia(Long periodoConferencia) {
        this.periodoConferencia = periodoConferencia;
    }

    public Long getPeriodoCredito() {
        return periodoCredito;
    }

    public void setPeriodoCredito(Long periodoCredito) {
        this.periodoCredito = periodoCredito;
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

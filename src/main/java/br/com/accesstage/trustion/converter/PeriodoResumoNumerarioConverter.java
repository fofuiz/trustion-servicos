package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.PeriodoResumoNumerarioDTO;
import br.com.accesstage.trustion.model.PeriodoResumoNumerario;

public class PeriodoResumoNumerarioConverter {

    public static PeriodoResumoNumerario paraEntidade(PeriodoResumoNumerarioDTO dto){

        if(dto == null)
            return null;

        PeriodoResumoNumerario entidade = new PeriodoResumoNumerario();
        entidade.setIdPeriodoResumo(dto.getIdPeriodoResumo());
        entidade.setIdUsuario(dto.getIdUsuario());
        entidade.setPeriodoColeta(dto.getPeriodoColeta());
        entidade.setPeriodoConferencia(dto.getPeriodoConferencia());
        entidade.setPeriodoCredito(dto.getPeriodoCredito());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());

        return entidade;
    }

    public static PeriodoResumoNumerarioDTO paraDTO(PeriodoResumoNumerario entidade){

        if(entidade == null)
            return null;

        PeriodoResumoNumerarioDTO dto = new PeriodoResumoNumerarioDTO();
        dto.setIdPeriodoResumo(entidade.getIdPeriodoResumo());
        dto.setIdUsuario(entidade.getIdUsuario());
        dto.setPeriodoColeta(entidade.getPeriodoColeta());
        dto.setPeriodoConferencia(entidade.getPeriodoConferencia());
        dto.setPeriodoCredito(entidade.getPeriodoCredito());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());

        return dto;

    }
}

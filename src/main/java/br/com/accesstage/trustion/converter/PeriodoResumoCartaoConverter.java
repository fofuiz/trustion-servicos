package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.PeriodoResumoCartaoDTO;
import br.com.accesstage.trustion.model.PeriodoResumoCartao;

public class PeriodoResumoCartaoConverter {

    public static PeriodoResumoCartao paraEntidade(PeriodoResumoCartaoDTO dto){

        if(dto == null)
            return null;

        PeriodoResumoCartao entidade = new PeriodoResumoCartao();
        entidade.setIdPeriodoResumo(dto.getIdPeriodoResumo());
        entidade.setIdUsuario(dto.getIdUsuario());
        entidade.setPeriodoVenda(dto.getPeriodoVenda());
        entidade.setPeriodoRecebimento(dto.getPeriodoRecebimento());
        entidade.setPeriodoRecebimentoFuturo(dto.getPeriodoRecebimentoFuturo());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());

        return entidade;
    }

    public static PeriodoResumoCartaoDTO paraDTO(PeriodoResumoCartao entidade){

        if(entidade == null)
            return null;

        PeriodoResumoCartaoDTO dto = new PeriodoResumoCartaoDTO();
        dto.setIdPeriodoResumo(entidade.getIdPeriodoResumo());
        dto.setIdUsuario(entidade.getIdUsuario());
        dto.setPeriodoVenda(entidade.getPeriodoVenda());
        dto.setPeriodoRecebimento(entidade.getPeriodoRecebimento());
        dto.setPeriodoRecebimentoFuturo(entidade.getPeriodoRecebimentoFuturo());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());

        return dto;
    }
}

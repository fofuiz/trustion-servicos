package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.ConciliadoDTO;
import br.com.accesstage.trustion.model.Conciliado;

public class ConciliadoConverter {

    public static ConciliadoDTO paraDTO(Conciliado entidade){

        if(entidade == null)
            return null;

        ConciliadoDTO dto = new ConciliadoDTO();

        dto.setIdConciliado(entidade.getIdConciliado());
        dto.setCnpj(entidade.getCnpj());
        dto.setDataLancamento(entidade.getDataLancamento());
        dto.setDataConciliacao(entidade.getDataConciliacao());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());

        return dto;
    }

    public static Conciliado paraEntidade(ConciliadoDTO dto){

        if(dto == null)
            return null;

        Conciliado entidade = new Conciliado();

        entidade.setIdConciliado(dto.getIdConciliado());
        entidade.setCnpj(dto.getCnpj());
        entidade.setDataLancamento(dto.getDataLancamento());
        entidade.setDataConciliacao(dto.getDataConciliacao());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());

        return entidade;
    }

}

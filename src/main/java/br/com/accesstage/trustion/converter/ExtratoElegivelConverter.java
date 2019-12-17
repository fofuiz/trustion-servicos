package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.ExtratoElegivelDTO;
import br.com.accesstage.trustion.model.ExtratoElegivel;

public class ExtratoElegivelConverter {

    public static ExtratoElegivel paraEntidade(ExtratoElegivelDTO dto){

        if(dto == null)
            return null;

        ExtratoElegivel entidade = new ExtratoElegivel();

        entidade.setIdExtElegivel(dto.getIdExtElegivel());
        entidade.setCodigoBanco(dto.getCodigoBanco());
        entidade.setConta(dto.getConta());
        entidade.setCnpj(dto.getCnpj());
        entidade.setAgencia(dto.getAgencia());
        entidade.setDataLancamento(dto.getDataLancamento());
        entidade.setValorLancamento(dto.getValorLancamento());
        entidade.setHistoricoLancamento(dto.getHistoricoLancamento());
        entidade.setIdConciliacao(dto.getIdConciliacao());

        return entidade;

    }

    public static ExtratoElegivelDTO paraDTO(ExtratoElegivel entidade){

        if(entidade == null)
            return null;

        ExtratoElegivelDTO dto = new ExtratoElegivelDTO();

        dto.setIdExtElegivel(entidade.getIdExtElegivel());
        dto.setCodigoBanco(entidade.getCodigoBanco());
        dto.setConta(entidade.getConta());
        dto.setCnpj(entidade.getCnpj());
        dto.setAgencia(entidade.getAgencia());
        dto.setDataLancamento(entidade.getDataLancamento());
        dto.setValorLancamento(entidade.getValorLancamento());
        dto.setHistoricoLancamento(entidade.getHistoricoLancamento());
        dto.setIdConciliacao(entidade.getIdConciliacao());

        return dto;

    }
}

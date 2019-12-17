package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.DetalheConferenciaDTO;
import br.com.accesstage.trustion.model.DetalheConferencia;

public class DetalheConferenciaConverter {

    public static DetalheConferencia paraEntidade(DetalheConferenciaDTO detalheConferenciaDTO) {

        DetalheConferencia entidade = new DetalheConferencia();
        entidade.setNumSerieEquipamento(detalheConferenciaDTO.getNumSerieEquipamento());
        entidade.setIdEquipamento(detalheConferenciaDTO.getIdEquipamento());
        entidade.setCnpjCliente(detalheConferenciaDTO.getCnpjCliente());
        entidade.setNumeroGVT(detalheConferenciaDTO.getNumeroGVT());
        entidade.setValorDeclarado(detalheConferenciaDTO.getValorDeclarado());
        entidade.setDataConferencia(detalheConferenciaDTO.getDataConferencia());
        entidade.setValorConferido(detalheConferenciaDTO.getValorConferido());
        entidade.setDataCriacao(detalheConferenciaDTO.getDataCriacao());
        entidade.setDataConferencia(detalheConferenciaDTO.getDataConferencia());
        entidade.setDataRecolhimento(detalheConferenciaDTO.getDataRecolhimento());

        return entidade;
    }

    public static DetalheConferenciaDTO paraDTO(DetalheConferencia entidade) {

        DetalheConferenciaDTO detalheConferenciaDTO = new DetalheConferenciaDTO();
        detalheConferenciaDTO.setNumSerieEquipamento(entidade.getNumSerieEquipamento());
        detalheConferenciaDTO.setIdEquipamento(entidade.getIdEquipamento());
        detalheConferenciaDTO.setCnpjCliente(entidade.getCnpjCliente());
        detalheConferenciaDTO.setNumeroGVT(entidade.getNumeroGVT());
        detalheConferenciaDTO.setValorDeclarado(entidade.getValorDeclarado());
        detalheConferenciaDTO.setDataConferencia(entidade.getDataConferencia());
        detalheConferenciaDTO.setValorConferido(entidade.getValorConferido());
        detalheConferenciaDTO.setDataCriacao(entidade.getDataCriacao());
        detalheConferenciaDTO.setDataConferencia(entidade.getDataConferencia());
        detalheConferenciaDTO.setDataRecolhimento(entidade.getDataRecolhimento());

        return detalheConferenciaDTO;
    }
}

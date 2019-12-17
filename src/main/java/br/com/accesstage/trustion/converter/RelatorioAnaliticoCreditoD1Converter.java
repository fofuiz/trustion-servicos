package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoD1DTO;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCreditoD1;

public class RelatorioAnaliticoCreditoD1Converter {

    public static RelatorioAnaliticoCreditoD1DTO paraDTO(RelatorioAnaliticoCreditoD1 entidade) {

        if (entidade == null) {
            return null;
        }

        RelatorioAnaliticoCreditoD1DTO dto = new RelatorioAnaliticoCreditoD1DTO();

        dto.setIdRelatorioAnalitico(entidade.getIdRelatorioAnalitico());
        dto.setIdGrupoEconomico(entidade.getIdGrupoEconomico());
        dto.setIdEmpresa(entidade.getIdEmpresa());
        dto.setCnpj(entidade.getCnpj());
        dto.setGtv(entidade.getGtv());
        dto.setCompartimento(entidade.getCompartimento());
        dto.setDataConferencia(entidade.getDataConferencia());
        dto.setValorConferido(entidade.getValorConferido());
        dto.setDataCredito(entidade.getDataCredito());
        dto.setValorCredito(entidade.getValorCredito());
        dto.setValorQuestionado(entidade.getValorQuestionado());
        dto.setStatusConciliacao(entidade.getStatusConciliacao());
        dto.setIdOcorrencia(entidade.getIdOcorrencia());
        dto.setDataStatusOcorrencia(entidade.getDataStatusOcorrencia());
        dto.setStatusOcorrencia(entidade.getStatusOcorrencia());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setIdConciliacao(entidade.getIdConciliacao());
        dto.setSiglaLoja(entidade.getSiglaLoja());
        dto.setDataColeta(entidade.getDataColeta());
        dto.setValorColeta(entidade.getValorColeta());
        dto.setIdTransportadora(entidade.getIdTransportadora());
        dto.setIdModeloNegocio(entidade.getIdModeloNegocio());

        return dto;
    }

    public static RelatorioAnaliticoCreditoD1 paraEntidade(RelatorioAnaliticoCreditoD1DTO dto) {

        if (dto == null) {
            return null;
        }

        RelatorioAnaliticoCreditoD1 entidade = new RelatorioAnaliticoCreditoD1();

        entidade.setIdRelatorioAnalitico(dto.getIdRelatorioAnalitico());
        entidade.setIdGrupoEconomico(dto.getIdGrupoEconomico());
        entidade.setIdEmpresa(dto.getIdEmpresa());
        entidade.setCnpj(dto.getCnpj());
        entidade.setGtv(dto.getGtv());
        entidade.setCompartimento(dto.getCompartimento());
        entidade.setDataConferencia(dto.getDataConferencia());
        entidade.setValorConferido(dto.getValorConferido());
        entidade.setDataCredito(dto.getDataCredito());
        entidade.setValorCredito(dto.getValorCredito());
        entidade.setValorQuestionado(dto.getValorQuestionado());
        entidade.setStatusConciliacao(dto.getStatusConciliacao());
        entidade.setIdOcorrencia(dto.getIdOcorrencia());
        entidade.setDataStatusOcorrencia(dto.getDataStatusOcorrencia());
        entidade.setStatusOcorrencia(dto.getStatusOcorrencia());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setIdConciliacao(dto.getIdConciliacao());
        entidade.setSiglaLoja(dto.getSiglaLoja());
        entidade.setDataColeta(dto.getDataColeta());
        entidade.setValorColeta(dto.getValorColeta());
        entidade.setIdTransportadora(dto.getIdTransportadora());
        entidade.setIdModeloNegocio(dto.getIdModeloNegocio());

        return entidade;
    }
}

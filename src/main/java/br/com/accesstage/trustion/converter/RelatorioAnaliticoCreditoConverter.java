package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoDTO;
import br.com.accesstage.trustion.model.RelatorioAnaliticoCredito;

public class RelatorioAnaliticoCreditoConverter {

    public static RelatorioAnaliticoCreditoDTO paraDTO(RelatorioAnaliticoCredito entidade) {

        RelatorioAnaliticoCreditoDTO dto = new RelatorioAnaliticoCreditoDTO();

        dto.setIdRelatorioAnalitico(entidade.getIdRelatorioAnalitico());
        dto.setIdGrupoEconomico(entidade.getIdGrupoEconomico());
        dto.setIdEmpresa(entidade.getIdEmpresa());
        dto.setDataCorte(entidade.getDataCorte());
        dto.setCodigoFechamento(entidade.getCodigoFechamento());
        dto.setIdOcorrencia(entidade.getIdOcorrencia());
        dto.setValorTotal(entidade.getValorTotal());
        dto.setValorQuestionado(entidade.getValorQuestionado());
        dto.setDataStatusOcorrencia(entidade.getDataStatusOcorrencia());
        dto.setStatusOcorrencia(entidade.getStatusOcorrencia());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setCnpj(entidade.getCnpjCliente());
        dto.setDataCredito(entidade.getDataCredito());
        dto.setValorCredito(entidade.getValorCredito());
        dto.setStatusConciliacao(entidade.getStatusConciliacao());
        dto.setIdEquipamento(entidade.getIdEquipamento());
        dto.setNumSerie(entidade.getNumSerie());
        dto.setIdConciliacao(entidade.getIdConciliacao());
        dto.setSiglaLoja(entidade.getSiglaLoja());
        dto.setIdTransportadora(entidade.getIdTransportadora());

        return dto;
    }

    public static RelatorioAnaliticoCredito paraEntidade(RelatorioAnaliticoCreditoDTO dto) {

        RelatorioAnaliticoCredito entidade = new RelatorioAnaliticoCredito();

        entidade.setIdRelatorioAnalitico(dto.getIdRelatorioAnalitico());
        entidade.setIdGrupoEconomico(dto.getIdGrupoEconomico());
        entidade.setIdEmpresa(dto.getIdEmpresa());
        entidade.setDataCorte(dto.getDataCorte());
        entidade.setCodigoFechamento(dto.getCodigoFechamento());
        entidade.setIdOcorrencia(dto.getIdOcorrencia());
        entidade.setValorTotal(dto.getValorTotal());
        entidade.setValorQuestionado(dto.getValorQuestionado());
        entidade.setDataStatusOcorrencia(dto.getDataStatusOcorrencia());
        entidade.setStatusOcorrencia(dto.getStatusOcorrencia());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setCnpjCliente(dto.getCnpj());
        entidade.setDataCredito(dto.getDataCredito());
        entidade.setValorCredito(dto.getValorCredito());
        entidade.setStatusConciliacao(dto.getStatusConciliacao());
        entidade.setIdEquipamento(dto.getIdEquipamento());
        entidade.setNumSerie(dto.getNumSerie());
        entidade.setIdConciliacao(dto.getIdConciliacao());
        entidade.setSiglaLoja(dto.getSiglaLoja());
        entidade.setIdTransportadora(dto.getIdTransportadora());

        return entidade;
    }
}

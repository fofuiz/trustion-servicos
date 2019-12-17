package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.ModeloNegocioDTO;
import br.com.accesstage.trustion.model.ModeloNegocio;
import br.com.accesstage.trustion.model.SlaAtendimento;

public class ModeloNegocioConverter {

    public static ModeloNegocio paraEntidade(ModeloNegocioDTO dto) {
        ModeloNegocio entidade = new ModeloNegocio();
        entidade.setIdModeloNegocio(dto.getIdModeloNegocio());
        entidade.setNome(dto.getNome());
        entidade.setDescricao(dto.getDescricao());
        entidade.setIdTipoServico(dto.getIdTipoServico());
        entidade.setIdTipoCredito(dto.getIdTipoCredito());
        entidade.setHorarioCorteCredito(dto.getHorarioCorteCredito());
        entidade.setHorarioCorteEnvioBanco(dto.getHorarioCorteEnvioBanco());
        entidade.setDataCriacao(dto.getDataCriacao());
        entidade.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
        entidade.setQuantidadeDiasConfCredito(dto.getQuantidadeDiasConfCredito());
        entidade.setIdTransportadora(dto.getIdTransportadora());
        return entidade;
    }

    public static ModeloNegocioDTO paraDTO(ModeloNegocio entidade) {
        ModeloNegocioDTO dto = new ModeloNegocioDTO();
        dto.setIdModeloNegocio(entidade.getIdModeloNegocio());
        dto.setNome(entidade.getNome());
        dto.setDescricao(entidade.getDescricao());
        dto.setIdTipoServico(entidade.getIdTipoServico());
        dto.setIdTipoCredito(entidade.getIdTipoCredito());
        dto.setHorarioCorteCredito(entidade.getHorarioCorteCredito());
        dto.setHorarioCorteEnvioBanco(entidade.getHorarioCorteEnvioBanco());
        dto.setDataCriacao(entidade.getDataCriacao());
        dto.setIdUsuarioCriacao(entidade.getIdUsuarioCriacao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setIdUsuarioAlteracao(entidade.getIdUsuarioAlteracao());
        dto.setQuantidadeDiasConfCredito(entidade.getQuantidadeDiasConfCredito());
        dto.setIdTransportadora(entidade.getIdTransportadora());
        return dto;
    }

    public static SlaAtendimento dtoParaSlaAtendimento(ModeloNegocioDTO dto) {
        SlaAtendimento entidade = new SlaAtendimento();
        entidade.setIdSlaAtendimento(dto.getIdSlaAtendimento());
        entidade.setIdModeloNegocio(dto.getIdModeloNegocio());
        entidade.setQtdDiasAnaliseSolicitada(dto.getQtdDiasAnaliseSolicitada());
        entidade.setQtdDiasAnaliseAndamento(dto.getQtdDiasAnaliseAndamento());
        entidade.setQtdDiasAnaliseAguarde(dto.getQtdDiasAnaliseAguarde());
        entidade.setAnaliseSolDiaUtil(dto.isAnaliseSolDiaUtil());
        entidade.setAnaliseAndDiaUtil(dto.isAnaliseAndDiaUtil());
        entidade.setAnaliseAguarDiaUtil(dto.isAnaliseAguarDiaUtil());
        return entidade;
    }

    public static ModeloNegocioDTO slaAtendimentoParaDTO(ModeloNegocioDTO dto, SlaAtendimento slaAtendimento) {
        dto.setQtdDiasAnaliseSolicitada(slaAtendimento.getQtdDiasAnaliseSolicitada());
        dto.setQtdDiasAnaliseAndamento(slaAtendimento.getQtdDiasAnaliseAndamento());
        dto.setQtdDiasAnaliseAguarde(slaAtendimento.getQtdDiasAnaliseAguarde());
        dto.setAnaliseSolDiaUtil(slaAtendimento.isAnaliseSolDiaUtil());
        dto.setAnaliseAndDiaUtil(slaAtendimento.isAnaliseAndDiaUtil());
        dto.setAnaliseAguarDiaUtil(slaAtendimento.isAnaliseAguarDiaUtil());
        dto.setIdSlaAtendimento(slaAtendimento.getIdSlaAtendimento());
        return dto;
    }
}

package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.TipoServicoDTO;
import br.com.accesstage.trustion.model.TipoServico;

public class TipoServicoConverter {

    public static TipoServico paraEntidade(TipoServicoDTO tipoServicoDTO) {

        TipoServico tipoServico = new TipoServico();
        tipoServico.setIdTipoServico(tipoServicoDTO.getIdTipoServico());
        tipoServico.setNomeServico(tipoServicoDTO.getNomeServico());
        tipoServico.setIdGrupoEconomico(tipoServicoDTO.getIdGrupoEconomico());
        tipoServico.setDescServico(tipoServicoDTO.getDescServico());
        tipoServico.setIdUsuarioCriacao(tipoServicoDTO.getIdUsuarioCriacao());
        tipoServico.setIdUsuarioAlteracao(tipoServicoDTO.getIdUsuarioAlteracao());
        tipoServico.setDataCriacao(tipoServicoDTO.getDataCriacao());
        tipoServico.setDataAlteracao(tipoServicoDTO.getDataAlteracao());

        return tipoServico;

    }

    public static TipoServicoDTO paraDTO(TipoServico tipoServico) {
        TipoServicoDTO tipoServicoDTO = new TipoServicoDTO();
        tipoServicoDTO.setIdTipoServico(tipoServico.getIdTipoServico());
        tipoServicoDTO.setNomeServico(tipoServico.getNomeServico());
        tipoServicoDTO.setIdGrupoEconomico(tipoServico.getIdGrupoEconomico());
        tipoServicoDTO.setDescServico(tipoServico.getDescServico());
        tipoServicoDTO.setIdUsuarioCriacao(tipoServico.getIdUsuarioCriacao());
        tipoServicoDTO.setIdUsuarioAlteracao(tipoServico.getIdUsuarioAlteracao());
        tipoServicoDTO.setDataCriacao(tipoServico.getDataCriacao());
        tipoServicoDTO.setDataAlteracao(tipoServico.getDataAlteracao());

        return tipoServicoDTO;
    }
}

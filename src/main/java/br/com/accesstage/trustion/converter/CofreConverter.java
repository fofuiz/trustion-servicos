package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.CofreDTO;
import br.com.accesstage.trustion.model.Cofre;

public class CofreConverter {

    public static CofreDTO paraDTO(Cofre cofre) {
        CofreDTO cofreDTO = new CofreDTO();
        cofreDTO.setIdCofre(cofre.getIdCofre());
        cofreDTO.setIdEquipamento(cofre.getIdEquipamento());
        cofreDTO.setNumSerie(cofre.getNumSerie());
        cofreDTO.setCodigoNaTransportadora(cofre.getCodCofreTransportadora());
        cofreDTO.setIdGrupoEconomico(cofre.getIdGrupoEconomico());
        cofreDTO.setIdEmpresa(cofre.getIdEmpresa());
        cofreDTO.setIdUsuarioCriacao(cofre.getIdUsuarioCriacao());
        cofreDTO.setIdUsuarioAlteracao(cofre.getIdUsuarioAlteracao());
        cofreDTO.setDataCriacao(cofre.getDataCriacao());
        cofreDTO.setDataAlteracao(cofre.getDataAlteracao());
        cofreDTO.setStatus(cofre.getStatus());
        cofreDTO.setIdModeloNegocio(cofre.getIdModeloNegocio());

        return cofreDTO;
    }

    public static Cofre paraEntidade(CofreDTO cofreDTO) {
        Cofre cofre = new Cofre();
        cofre.setIdCofre(cofreDTO.getIdCofre());
        cofre.setIdEquipamento(cofreDTO.getIdEquipamento());
        cofre.setNumSerie(cofreDTO.getNumSerie());
        cofre.setCodCofreTransportadora(cofreDTO.getCodigoNaTransportadora());
        cofre.setIdGrupoEconomico(cofreDTO.getIdGrupoEconomico());
        cofre.setIdEmpresa(cofreDTO.getIdEmpresa());
        cofre.setIdUsuarioCriacao(cofreDTO.getIdUsuarioCriacao());
        cofre.setIdUsuarioAlteracao(cofreDTO.getIdUsuarioAlteracao());
        cofre.setDataCriacao(cofreDTO.getDataCriacao());
        cofre.setDataAlteracao(cofreDTO.getDataAlteracao());
        cofre.setStatus(cofreDTO.getStatus());
        cofre.setIdModeloNegocio(cofreDTO.getIdModeloNegocio());

        return cofre;
    }
}

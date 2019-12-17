package br.com.accesstage.trustion.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import br.com.accesstage.trustion.dto.ColetaGTVDTO;
import br.com.accesstage.trustion.model.ColetaGTV;

public class ColetaGTVConverter {

    public static ColetaGTV paraEntidade(ColetaGTVDTO coletaGTVDTO) {

        ColetaGTV entidade = new ColetaGTV();
        entidade.setIdColetaGTV(coletaGTVDTO.getIdColetaGTV());
        entidade.setNumSerie(coletaGTVDTO.getNumSerie());
        entidade.setIdEquipamento(coletaGTVDTO.getIdEquipamento());
        entidade.setCnpjCliente(coletaGTVDTO.getCnpjCliente());
        entidade.setPeriodoInicialDt(coletaGTVDTO.getPeriodoInicialDt() == null ? null : LocalDateTime.ofInstant(coletaGTVDTO.getPeriodoInicialDt().toInstant(), ZoneId.systemDefault()));
        entidade.setColetaDt(coletaGTVDTO.getColetaDt() == null ? null : LocalDateTime.ofInstant(coletaGTVDTO.getColetaDt().toInstant(), ZoneId.systemDefault()));
        entidade.setValorTotal(coletaGTVDTO.getValorTotal());
        entidade.setNumeroGVT(coletaGTVDTO.getNumeroGVT());
        entidade.setDataCriacao(coletaGTVDTO.getDataCriacao());

        return entidade;
    }

    public static ColetaGTVDTO paraDTO(ColetaGTV coletaGTV) {

        ColetaGTVDTO coletaGTVDTO = new ColetaGTVDTO();
        coletaGTVDTO.setIdColetaGTV(coletaGTV.getIdColetaGTV());
        coletaGTVDTO.setNumSerie(coletaGTV.getNumSerie());
        coletaGTVDTO.setIdEquipamento(coletaGTV.getIdEquipamento());
        coletaGTVDTO.setCnpjCliente(coletaGTV.getCnpjCliente());
        coletaGTVDTO.setPeriodoInicialDt(coletaGTV.getPeriodoInicialDt() == null ? null : Date.from(coletaGTV.getPeriodoInicialDt().atZone(ZoneId.systemDefault()).toInstant()));
        coletaGTVDTO.setColetaDt(coletaGTV.getColetaDt() == null ? null : Date.from(coletaGTV.getColetaDt().atZone(ZoneId.systemDefault()).toInstant()));
        coletaGTVDTO.setValorTotal(coletaGTV.getValorTotal());
        coletaGTVDTO.setNumeroGVT(coletaGTV.getNumeroGVT());
        coletaGTVDTO.setDataCriacao(coletaGTV.getDataCriacao());

        return coletaGTVDTO;
    }

}

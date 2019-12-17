package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.EmpresaSegmentoDTO;
import br.com.accesstage.trustion.model.EmpresaSegmento;

public class EmpresaSegmentoConverter {

    public static EmpresaSegmento paraEntidate(EmpresaSegmentoDTO dto) {

        EmpresaSegmento tipo = new EmpresaSegmento();
        tipo.setIdEmpresaSegmento(dto.getIdEmpresaSegmento());
        tipo.setDescricao(dto.getDescricao());

        return tipo;
    }

    public static EmpresaSegmentoDTO paraDTO(EmpresaSegmento tipo) {

        EmpresaSegmentoDTO dto = new EmpresaSegmentoDTO();
        dto.setIdEmpresaSegmento(tipo.getIdEmpresaSegmento());
        dto.setDescricao(tipo.getDescricao());

        return dto;
    }

}

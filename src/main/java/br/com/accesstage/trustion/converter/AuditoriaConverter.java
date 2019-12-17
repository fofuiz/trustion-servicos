package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.AuditoriaDTO;
import br.com.accesstage.trustion.model.Auditoria;

public class AuditoriaConverter {

    public static Auditoria paraEntidade(AuditoriaDTO dto) {
        Auditoria auditoria = new Auditoria();
        auditoria.setIdGrupoEconomico(dto.getIdGrupoEconomico());
        auditoria.setIdEmpresa(dto.getIdEmpresa());
        auditoria.setIdUsuario(dto.getIdUsuario());
        auditoria.setDataAcao(dto.getDataInicial());
        auditoria.setAcao(dto.getAcao());
        auditoria.setNroOcorrencia(dto.getNroOcorrencia());
        return auditoria;
    }

    public static AuditoriaDTO paraDTO(Auditoria auditoria) {
        AuditoriaDTO dto = new AuditoriaDTO();
        dto.setIdAuditoria(auditoria.getIdAuditoria());
        dto.setIdGrupoEconomico(auditoria.getIdGrupoEconomico());
        dto.setIdEmpresa(auditoria.getIdEmpresa());
        dto.setIdUsuario(auditoria.getIdUsuario());
        dto.setDataInicial(auditoria.getDataAcao());
        dto.setAcao(auditoria.getAcao());
        dto.setNroOcorrencia(auditoria.getNroOcorrencia());
        return dto;
    }
}

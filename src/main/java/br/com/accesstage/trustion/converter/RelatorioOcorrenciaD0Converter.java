package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.RelatorioOcorrenciaDTO;
import br.com.accesstage.trustion.model.RelatorioOcorrenciaD0;

public class RelatorioOcorrenciaD0Converter {

	public static RelatorioOcorrenciaDTO paraDTO(RelatorioOcorrenciaD0 entidade) {

		RelatorioOcorrenciaDTO dto = new RelatorioOcorrenciaDTO();
		// dto.setIdEmpresa(entidade.getIdEmpresa());
		dto.setCnpjCliente(entidade.getCnpjCliente());
		dto.setIdOcorrencia(entidade.getIdOcorrencia());
		dto.setDataCriacao(entidade.getDataCriacao());
		dto.setStatusOcorrencia(entidade.getStatusOcorrencia());
		dto.setId_sla_atendimento(entidade.getId_sla_atendimento());
//		dto.setQuantidadeDiasSla(entidade.tQuantidadeDiasSla());
		dto.setIdModeloNegocio(entidade.getIdModeloNegocio());
		dto.setDataStatusOcorrencia(entidade.getDataStatusOcorrencia());
		dto.setStatusOcorrencia(entidade.getStatusOcorrencia());
		dto.setRazaoSocial(entidade.getRazaoSocial());
		dto.setResponsavel(entidade.getResponsavel());
		dto.setIdGrupoEconomicoOutrasEmpresas(entidade.getIdGrupoEconomicoOutrasEmpresas());

		return dto;
	}

}

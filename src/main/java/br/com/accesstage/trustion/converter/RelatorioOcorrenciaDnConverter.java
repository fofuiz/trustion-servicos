package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.RelatorioOcorrenciaDNDTO;
import br.com.accesstage.trustion.model.RelatorioOcorrenciaDN;

public class RelatorioOcorrenciaDnConverter {

	public static RelatorioOcorrenciaDNDTO paraDTO(RelatorioOcorrenciaDN entidade) {

		RelatorioOcorrenciaDNDTO dto = new RelatorioOcorrenciaDNDTO();
		// dto.setIdEmpresa(entidade.getIdEmpresa());
		dto.setCnpjCliente(entidade.getCnpjCliente());
		dto.setIdOcorrencia(entidade.getIdOcorrencia());
		dto.setDataCriacao(entidade.getDataCriacao());
		dto.setStatusOcorrencia(entidade.getStatusOcorrencia());
		dto.setId_sla_atendimento(entidade.getId_sla_atendimento());
		// dto.setQuantidadeDiasSla(entidade.tQuantidadeDiasSla());
		dto.setIdModeloNegocio(entidade.getIdModeloNegocio());
		dto.setDataStatusOcorrencia(entidade.getDataStatusOcorrencia());
		dto.setStatusOcorrencia(entidade.getStatusOcorrencia());
		dto.setRazaoSocial(entidade.getRazaoSocial());
		dto.setResponsavel(entidade.getResponsavel());
		dto.setGtv(entidade.getGtv());
		dto.setIdGrupoEconomicoOutrasEmpresas(entidade.getIdGrupoEconomicoOutrasEmpresas());

		return dto;
	}

}

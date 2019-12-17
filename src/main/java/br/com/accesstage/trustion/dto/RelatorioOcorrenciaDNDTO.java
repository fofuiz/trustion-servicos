package br.com.accesstage.trustion.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RelatorioOcorrenciaDNDTO {

    	private Long idGrupoEconomico;
        private List<Long> idGrupoEconomicoList;
	private List<Long> idsEmpresa;
	private String cnpjCliente;
        private List<String> cnpjClienteList;
	private Long idOcorrencia;
	private String statusOcorrencia;
	private Date dataStatusOcorrencia;
	private Long idModeloNegocio;
	private Integer quantidadeDiasSla;
	private Long id_sla_atendimento;
	private Date dataCriacao;
	private String razaoSocial;	
	private String gtv;
	private String responsavel;
	private String descricaoModeloNegocio;
	private Long diasPendentes;
	private Long diasEmAberto;
	private String farol;	
	private List<String> farois ;
	private Long idGrupoEconomicoOutrasEmpresas;
        private List<Long> idGrupoEconomicoOutrasEmpresasList;

	

	@NotNull
	private Date dataInicial;

	@NotNull
	private Date dataFinal;

	


}

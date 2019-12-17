package br.com.accesstage.trustion.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper=true, includeFieldNames=true)
public class RelatorioOcorrenciaDTO {

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

    private String responsavel;
    private String nomeModeloNegocio;
    private String descricaoModeloNegocio;
    private Long diasPendentes;
    private Long diasEmAberto;
    private String farol;

    private List<String> farois;
    private Long idGrupoEconomicoOutrasEmpresas;
    private List<Long> idGrupoEconomicoOutrasEmpresasList;

    private Long rowNumber;
    private Long idEmpresa;
    private String cnpjEmpresa;
    private Integer totalRegistros;
    private Integer totalRegistrosPesquisaPorId;

    @NotNull
    private Date dataInicial;

    @NotNull
    private Date dataFinal;

    private Long idTransportadora;
    private List<Long> idsTransportadoras;
    private String razaoSocialTransportadora;
    private String gtv;

}

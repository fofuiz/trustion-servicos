package br.com.accesstage.trustion.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioAnaliticoCreditoDTO {

    private Long idRelatorioAnalitico;

    private Long idGrupoEconomico;

    private String grupoEconomico;

    private Long idTransportadora;

    private String transportadora;

    @NotNull
    private List<TransportadoraDTO> transportadoras;

    @NotNull
    private List<GrupoEconomicoDTO> outrasEmpresas;

    private List<GrupoEconomicoDTO> grupo;

    private Long idEmpresa;

    private String empresa;

    private String cnpj;

    @NotNull
    private List<EmpresaDTO> empresas;

    private List<CofreDTO> cofres;

    private Long idEquipamento;

    private String numSerie;

    private Date dataCorte;

    private Long codigoFechamento;

    private Long idOcorrencia;

    private BigDecimal valorTotal;

    private BigDecimal valorQuestionado;

    private BigDecimal diferencaValorQuestionado;

    private Boolean isValorRegistradoLink;

    @NotNull
    private Date dataInicial;

    @NotNull
    private Date dataFinal;

    private Date dataStatusOcorrencia;

    private String statusOcorrencia;

    private Date dataCredito;

    private BigDecimal valorCredito;

    private String statusConciliacao;

    private Date dataCriacao;

    private Long idUsuarioCriacao;

    private Date dataAlteracao;

    private Long idUsuarioAlteracao;

    private Long idConciliacao;

    private String siglaLoja;

    private String responsavel;

    private boolean registrosComDiferenca;

}

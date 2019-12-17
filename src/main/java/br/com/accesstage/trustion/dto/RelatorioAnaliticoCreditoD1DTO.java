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
public class RelatorioAnaliticoCreditoD1DTO {

    private Long idRelatorioAnalitico;

    private Long idGrupoEconomico;

    private String grupoEconomico;

    private Long idTransportadora;

    private String transportadora;
    
    private List<TransportadoraDTO> transportadoras;

    private List<GrupoEconomicoDTO> outrasEmpresas;

    private Long idEmpresa;

    private String empresa;

    private List<EmpresaDTO> empresas;
    
    private Long idModeloNegocio;

    private String cnpj;

    private String gtv;

    private boolean gtvLink;

    private String compartimento;

    private Date dataConferencia;

    private BigDecimal valorConferido;

    private Date dataCredito;

    private BigDecimal valorCredito;

    private BigDecimal valorQuestionado;

    private BigDecimal diferencaValorQuestionado;

    private String statusConciliacao;

    private Long idOcorrencia;

    private Date dataStatusOcorrencia;

    private String statusOcorrencia;

    private Date dataCriacao;

    @NotNull
    private Date dataInicial;

    @NotNull
    private Date dataFinal;

    private Long idConciliacao;

    private String siglaLoja;

    private Date dataColeta;

    private BigDecimal valorColeta;

    private String responsavel;

    private boolean registrosComDiferenca;

}

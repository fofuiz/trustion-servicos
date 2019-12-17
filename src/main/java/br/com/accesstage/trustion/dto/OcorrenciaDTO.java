package br.com.accesstage.trustion.dto;

import br.com.accesstage.trustion.model.Ocorrencia;
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
public class OcorrenciaDTO {

    private Long idRelatorioAnalitico;
    private Long idOcorrencia;
    private String grupoEconomico;
    private String empresa;
    private String modeloNegocio;
    private Date dataCorte;
    private Date dataCredito;
    private BigDecimal valorRegistradoCofre;
    private Boolean isValorRegistradoCofreLink;
    private BigDecimal valorCreditadoConta;
    private Long idEmpresa;
    private Long idGrupoEconomico;
    private String observacao;
    @NotNull
    private Long idTipoQuestionamento;
    private String tipoQuestionamento;
    @NotNull
    private BigDecimal valorQuestionado;

    private Boolean isOcorrenciaSelected;

    private BigDecimal valorAjuste;

    private Long idTipoStatusOcorrencia;
    private String tipoStatusOcorrencia;
    private Date dataStatusOcorrencia;

    private Long idOcorrenciaMescla;

    private String statusConciliacao;
    private Long idUsuarioCriacao;
    private Date dataCriacao;
    private Long idUsuarioAlteracao;
    private Date dataAlteracao;
    private Boolean exibirArqResOcorrencia;
    private String arquivoResumoOcorrencia;

    private List<Ocorrencia> mesclas;

    private boolean concluido;

    private Long idTransportadora;
    private String nmeTransportadora;
    
    private Boolean isOcorrenciaD1;
    
    // Atributos especificos DN
    private Date dataConferencia;
    private BigDecimal valorDeclarado;
    private BigDecimal valorConferido;
    private BigDecimal valorColeta;
    private Date dataColeta;
    
}

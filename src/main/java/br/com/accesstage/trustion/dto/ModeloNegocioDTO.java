package br.com.accesstage.trustion.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ModeloNegocioDTO {

    private Long idModeloNegocio;

    @NotEmpty
    @NotBlank
    @Length(max = 100)
    private String nome;

    @NotEmpty
    @NotBlank
    @Length(max = 300)
    private String descricao;

    private Long idTipoServico;

    private String tipoServico;

    @NotNull
    private Long idTipoCredito;

    private String tipoCredito;

    private Integer quantidadeDiasConfCredito;

    private Date horarioCorteCredito;

    private Date horarioCorteEnvioBanco;

    private Date dataCriacao;

    private Long idUsuarioCriacao;

    private Date dataAlteracao;

    private Long idUsuarioAlteracao;

    @NotNull
    private Integer qtdDiasAnaliseSolicitada;

    @NotNull
    private Integer qtdDiasAnaliseAndamento;

    @NotNull
    private Integer qtdDiasAnaliseAguarde;

    private boolean analiseSolDiaUtil;

    private boolean analiseAndDiaUtil;

    private boolean analiseAguarDiaUtil;

    private Long idSlaAtendimento;

    @NotNull
    private Long idTransportadora;
    
    private String transportadora;
}
package br.com.accesstage.trustion.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CofreDTO {

    private Long idCofre;

    @NotNull
    private Long idEquipamento;

    @NotEmpty
    @NotBlank
    private String numSerie;

    private Long codigoNaTransportadora;

    @NotNull
    private Long idGrupoEconomico;

    @NotNull
    private Long idEmpresa;

    private String nomeGrupoEconomico;

    private String razaoSocial;

    private Date dataCriacao;

    private Long idUsuarioCriacao;

    private Date dataAlteracao;

    private Long idUsuarioAlteracao;

    private String status;

    @NotNull
    private Long idModeloNegocio;
}
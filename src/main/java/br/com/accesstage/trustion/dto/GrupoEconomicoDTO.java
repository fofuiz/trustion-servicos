package br.com.accesstage.trustion.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GrupoEconomicoDTO {

    private Long idGrupoEconomico;

    @NotNull
    @NotEmpty
    @NotBlank
    private String nome;

    @NotNull
    @NotEmpty
    @NotBlank
    private String cnpj;

    private Date dataCriacao;

    private Long idUsuarioCriacao;

    private Date dataAlteracao;

    private Long idUsuarioAlteracao;

    private boolean isCriacaoForm;

}

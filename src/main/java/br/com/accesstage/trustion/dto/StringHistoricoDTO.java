package br.com.accesstage.trustion.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StringHistoricoDTO {

    private Long idStringHistorico;

    @NotNull
    private Long idListaBanco;

    private String banco;

    @NotNull
    @NotEmpty
    @Length(max = 25)
    private String texto;

    @NotNull
    @NotEmpty
    private String status;

    private Date dataCriacao;

    private Long idUsuarioCriacao;

    private Date dataAlteracao;

    private Long idUsuarioAlteracao;
}

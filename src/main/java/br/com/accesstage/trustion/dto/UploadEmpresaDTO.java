package br.com.accesstage.trustion.dto;

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
public class UploadEmpresaDTO {

    @NotNull
    private Long idGrupoEconomico;

    @NotNull
    private List<Long> idModeloNegocio;

}

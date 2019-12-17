package br.com.accesstage.trustion.dto.ascartoes;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroTaxaAdministrativaDTO {

    @NotNull
    private Integer idsEmp;
    private Long codLoja;
    @NotNull
    private String dataInicial;
    @NotNull
    private String dataFinal;
    private String tipo;

}

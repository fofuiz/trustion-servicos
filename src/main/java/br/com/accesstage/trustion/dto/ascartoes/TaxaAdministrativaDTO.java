package br.com.accesstage.trustion.dto.ascartoes;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxaAdministrativaDTO {

    private String loja;
    private String pontoVenda;
    private String operadora;
    private String produto;
    private BigDecimal plano;
    private Double taxa;
    private Double txCadastrada;
    private String tipo;
    private Integer totalRegistros;

}

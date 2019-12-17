package br.com.accesstage.trustion.dto.ascartoes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroTaxasAdministrativasCadastroDTO {
    private Long empId;
    private Long codLoja;
    private Long codPontoVenda;
    private Long codOperadora;
    private Long codProduto;
}

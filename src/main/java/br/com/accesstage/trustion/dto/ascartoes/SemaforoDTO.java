package br.com.accesstage.trustion.dto.ascartoes;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author carlos
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemaforoDTO implements Serializable {

    private Integer quantidade;
    private Integer status;

    private BigDecimal valor;

    private Long totalQuantidade;
    private BigDecimal totalValor;
}

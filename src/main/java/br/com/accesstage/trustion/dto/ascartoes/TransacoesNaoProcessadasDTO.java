package br.com.accesstage.trustion.dto.ascartoes;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransacoesNaoProcessadasDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String numeroUnico;
    private String codigoLote;
    private String codDataVenda;
    private String codDataCredito;
    private BigDecimal valorLiquido;
    private String qtdCvsAceitos;
    private Long totalPaginas;
    private Long codigoEstabelecimentoSubmissor;

    private BigDecimal valorLiquidoAntecipado;
    private Long numeroAntecipacao;
}

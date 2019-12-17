package br.com.accesstage.trustion.dto.ascartoes;

import br.com.accesstage.trustion.util.Funcoes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemessaConciliacaoDetalheDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int rowKey;
    private Long codLoja;
    private Long codOperadora;
    private Long nroPlano;
    private Long codNsu;
    private Long codDetalhe;
    @NotNull
    private Long empId;
    private Long quantidadeItensAgrupado;
    private Date dtaVenda;
    private String nmeLoja;
    private String nomeProduto;
    private String dscAutorizacao;
    private String userExclusao;
    private String dscAreaCliente;
    private String hashValue;
    private String vlrBrutoDesc;
    private BigDecimal vlrBruto;
    private BigDecimal vlrLiquido;

    public double getVlrBrutoDesc() {
        double vlrBrutoConveter = 0D;
        if (vlrBruto != null) {
            vlrBrutoConveter = Funcoes.dividePorCem(vlrBruto.doubleValue());
        }
        return vlrBrutoConveter;
    }
}

package br.com.accesstage.trustion.dto.ascartoes;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Funcoes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GestaoVendasDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Date data;
    private String dataVendaFormatada;
    private String loja;
    private String bandeira;
    private String produto;
    private String plano;
    private BigDecimal valor;
    @NotNull
    private String nsu;
    @NotNull
    private String codAutorizacao;
    private String caixa;
    private String captura;
    private Long totalPaginas;
    private Long totalRegistros;
    private Integer parcela;
    private boolean selecionado;
    private Long codOperadora;
    private String dscOperadora;
    private Long codProduto;
    private Long codPontoVenda;
    private Long idDetalhe;
    private Integer dataVenda;
    private Date dtaVendaDt;
    private String dscAreaCliente;
    @NotNull
    private Integer statusConciliacao;
    private String nomeArquivo;
    private String sequencial;
    private Long dataVendaLong;
    private String hashValue;
    private String codArquivo;
    private String tidTransacao;
    @NotNull
    private List<String> listaEmpresas;

    @Log
    private static Logger LOGGER;

    public void setDataVenda(Integer dataVenda) {
        if (dataVenda != null && this.dtaVendaDt != null) {
            this.dtaVendaDt = Funcoes.formataDataRelatorio(dataVenda.longValue());
        }
    }

}

package br.com.accesstage.trustion.dto.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import static java.util.Arrays.asList;

/**
 *
 * @author raphael
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroGestaoVendaDTO implements Serializable {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataInicial;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataFinal;
    private List<String> listaEmpresas;
    private Long codLojaOuPv;
    private int statusConciliacao;
    @NotNull
    private Integer idVisao;
    private Long codArquivo;
    private EmpresaCA empresa;

    // Filtro da tabela
    private String filtroLoja;
    private String filtroOperadora;
    private String filtroProduto;
    private String filtroPlano;
    private String filtroValor;
    private String filtroNsu;
    private String filtroAutorizacao;
    private String filtroAreaCliente;

    // Paginação
    private Integer paginacaoFirst;
    private Integer paginacaoPageSize;
    private String paginacaoSortField;
    private Boolean paginacaoAscOrder;

    public PaginacaoDTO getPaginacaoDTO() {
        if (!asList(paginacaoFirst, paginacaoPageSize, paginacaoSortField, paginacaoAscOrder).contains(null)) {
            return new PaginacaoDTO(paginacaoFirst, paginacaoPageSize, paginacaoSortField, paginacaoAscOrder);
        }
        return null;
    }
}

package br.com.accesstage.trustion.dto.ascartoes;

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
public class FiltroBaixaDTO {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dataInicial;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dataFinal;
    @NotNull
    private List<String> listaEmpresas;
    private Long codLojaOuPv;
    private Integer tpoConciliacao;

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

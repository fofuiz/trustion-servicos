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
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import static java.util.Arrays.asList;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FiltroTransacoesNaoProcessadasDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dataInicial;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dataFinal;
    @NotNull
    private Long opcaoExtrato;
    private Long idOperadora;
    private List<String> listaEmpresas;
    private EmpresaCA empresa;

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

package br.com.accesstage.trustion.loader;

import br.com.accesstage.trustion.dto.ascartoes.FiltroTransacoesNaoProcessadasDTO;
import br.com.accesstage.trustion.dto.ascartoes.TransacoesNaoProcessadasDTO;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import java.math.BigDecimal;
import java.util.Date;

import static java.util.Arrays.asList;

/**
 *
 * @author raphael
 */
public class TransacoesNaoProcessadasLoader implements TemplateLoader {

    private static final int DEFAULT_RANGE_VALUE = 500;

    @Override
    public void load() {

        Fixture.of(FiltroTransacoesNaoProcessadasDTO.class).addTemplate("valid", new Rule() {
            {
                add("dataInicial", new Date());
                add("dataFinal", new Date());
                add("opcaoExtrato", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("idOperadora", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("listaEmpresas", asList("Str917", "Str984", "Str1017"));
                add("empresa", null);
                add("paginacaoFirst", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("paginacaoPageSize", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("paginacaoSortField", random("paginacaoSortField1", "paginacaoSortField2", "paginacaoSortField3"));
                add("paginacaoAscOrder", random(true, false));
            }
        });

        Fixture.of(TransacoesNaoProcessadasDTO.class).addTemplate("valid", new Rule() {
            {
                add("id", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("numeroUnico", random("numeroUnico1", "numeroUnico2", "numeroUnico3"));
                add("codigoLote", random("codigoLote1", "codigoLote2", "codigoLote3"));
                add("codDataVenda", random("codDataVenda1", "codDataVenda2", "codDataVenda3"));
                add("codDataCredito", random("codDataCredito1", "codDataCredito2", "codDataCredito3"));
                add("valorLiquido", random(BigDecimal.class, range(1, DEFAULT_RANGE_VALUE)));
                add("qtdCvsAceitos", random("qtdCvsAceitos1", "qtdCvsAceitos2", "qtdCvsAceitos3"));
                add("totalPaginas", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("codigoEstabelecimentoSubmissor", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("valorLiquidoAntecipado", random(BigDecimal.class, range(1, DEFAULT_RANGE_VALUE)));
                add("numeroAntecipacao", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
            }
        });
    }
}

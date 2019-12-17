package br.com.accesstage.trustion.loader;

import br.com.accesstage.trustion.dto.ascartoes.FiltroGestaoVendaDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.dto.ascartoes.SemaforoDTO;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.Arrays.asList;

/**
 *
 * @author raphael
 */
public class GestaoVendaLoader implements TemplateLoader {

    private static final int DEFAULT_RANGE_VALUE = 500;

    @Override
    public void load() {

        Fixture.of(FiltroGestaoVendaDTO.class).addTemplate("valid", new Rule() {
            {
                add("dataInicial", randomDate("2018-08-24", "2018-11-29", new SimpleDateFormat("yyyy-MM-dd")));
                add("dataFinal", randomDate("2018-08-24", "2018-11-29", new SimpleDateFormat("yyyy-MM-dd")));
                add("listaEmpresas", asList("Str1721", "Str34", "Str329"));
                add("codLojaOuPv", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("statusConciliacao", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("idVisao", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("codArquivo", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("empresa", null);
                add("filtroLoja", random("filtroLoja1", "filtroLoja2", "filtroLoja3"));
                add("filtroOperadora", random("filtroOperadora1", "filtroOperadora2", "filtroOperadora3"));
                add("filtroProduto", random("filtroProduto1", "filtroProduto2", "filtroProduto3"));
                add("filtroPlano", random("filtroPlano1", "filtroPlano2", "filtroPlano3"));
                add("filtroValor", random("filtroValor1", "filtroValor2", "filtroValor3"));
                add("filtroNsu", random("filtroNsu1", "filtroNsu2", "filtroNsu3"));
                add("filtroAutorizacao", random("filtroAutorizacao1", "filtroAutorizacao2", "filtroAutorizacao3"));
                add("filtroAreaCliente", random("filtroAreaCliente1", "filtroAreaCliente2", "filtroAreaCliente3"));
                add("paginacaoFirst", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("paginacaoPageSize", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("paginacaoSortField", random("paginacaoSortField1", "paginacaoSortField2", "paginacaoSortField3"));
                add("paginacaoAscOrder", random(true, false));
            }
        });

        Fixture.of(GestaoVendasDTO.class).addTemplate("valid", new Rule() {
            {
                add("id", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("data", new Date());
                add("dataVendaFormatada", random("dataVendaFormatada1", "dataVendaFormatada2", "dataVendaFormatada3"));
                add("loja", random("loja1", "loja2", "loja3"));
                add("bandeira", random("bandeira1", "bandeira2", "bandeira3"));
                add("produto", random("produto1", "produto2", "produto3"));
                add("plano", random("plano1", "plano2", "plano3"));
                add("valor", random(BigDecimal.class, range(1, DEFAULT_RANGE_VALUE)));
                add("nsu", random("nsu1", "nsu2", "nsu3"));
                add("codAutorizacao", random("codAutorizacao1", "codAutorizacao2", "codAutorizacao3"));
                add("caixa", random("caixa1", "caixa2", "caixa3"));
                add("captura", random("captura1", "captura2", "captura3"));
                add("totalPaginas", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("totalRegistros", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("parcela", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("selecionado", random(true, false));
                add("codOperadora", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("dscOperadora", random("dscOperadora1", "dscOperadora2", "dscOperadora3"));
                add("codProduto", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("codPontoVenda", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("idDetalhe", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("dataVenda", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("dtaVendaDt", new Date());
                add("dscAreaCliente", random("dscAreaCliente1", "dscAreaCliente2", "dscAreaCliente3"));
                add("statusConciliacao", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("nomeArquivo", random("nomeArquivo1", "nomeArquivo2", "nomeArquivo3"));
                add("sequencial", random("sequencial1", "sequencial2", "sequencial3"));
                add("dataVendaLong", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("hashValue", random("hashValue1", "hashValue2", "hashValue3"));
                add("codArquivo", random("codArquivo1", "codArquivo2", "codArquivo3"));
                add("tidTransacao", random("tidTransacao1", "tidTransacao2", "tidTransacao3"));
                add("listaEmpresas", asList("Str67", "Str428", "Str163"));
            }
        });

        Fixture.of(SemaforoDTO.class).addTemplate("valid", new Rule() {
            {
                add("quantidade", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("status", random(Integer.class, range(1, DEFAULT_RANGE_VALUE)));
                add("valor", random(BigDecimal.class, range(1, DEFAULT_RANGE_VALUE)));
                add("totalQuantidade", random(Long.class, range(1L, DEFAULT_RANGE_VALUE)));
                add("totalValor", random(BigDecimal.class, range(1, DEFAULT_RANGE_VALUE)));
            }
        });
    }
}

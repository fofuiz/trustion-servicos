package br.com.accesstage.trustion.loader;

import br.com.accesstage.trustion.ascartoes.model.LojaCA;
import br.com.accesstage.trustion.ascartoes.model.OperadoraCA;
import br.com.accesstage.trustion.ascartoes.model.PontoVendaCA;
import br.com.accesstage.trustion.ascartoes.model.ProdutoCA;
import br.com.accesstage.trustion.ascartoes.model.TaxaAdministrativa;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAdministrativaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxasAdministrativasCadastroDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaCadastroDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaDTO;
import br.com.accesstage.trustion.enums.ascartoes.TaxaAdministrativaPlanoEnum;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxaAdministrativaLoader implements TemplateLoader {

    @Override
    public void load() {

        Fixture.of(FiltroTaxaAdministrativaDTO.class).addTemplate("valid", new Rule() {
            {
                add("idsEmp", random(Integer.class, range(1, 200)));
                add("dataInicial", random("20170101", "20171001"));
                add("dataFinal", random("20180101", "20181001"));
            }
        });

        Fixture.of(TaxaAdministrativaDTO.class).addTemplate("valid", new Rule() {
            {
                add("loja", random("loja1", "loja2"));
                add("pontoVenda", random("pontoVenda1", "pontoVenda2"));
                add("operadora", random("oper1", "oper2"));
                add("produto", random("produto1", "produto2"));
            }
        });

        Fixture.of(FiltroTaxasAdministrativasCadastroDTO.class).addTemplate("valid", new Rule() {
            {
                add("codLoja", random(Long.class, range(1, 200)));
                add("codPontoVenda", random(Long.class, range(1, 200)));
                add("codOperadora", random(Long.class, range(1, 200)));
                add("codProduto", random(Long.class, range(1, 200)));
                add("empId", random(Long.class, range(1, 200)));
            }
        });

        List<TaxaAdministrativaPlanoEnum> l = new ArrayList<>();
        l.add(TaxaAdministrativaPlanoEnum.PLANO_1);

        Fixture.of(TaxaAdministrativaCadastroDTO.class).addTemplate("valid", new Rule() {
            {
                add("codTaxaAdministrativa", random(Long.class, range(1, 200)));
                add("codEmp", random(Long.class, range(1, 200)));
                add("codLoja", random(Long.class, range(1, 200)));
                add("codOperadora", random(Long.class, range(1, 200)));
                add("codProduto", random(Long.class, range(1, 200)));
                add("nmeLoja", random("nmeLoja1", "nmeLoja2"));
                add("nmeOperadora", random("nmeOperadora1", "nmeOperadora2"));
                add("nmeProduto", random("nmeProduto1", "nmeProduto2"));
                add("codUsuario", random(Long.class, range(1, 200)));
                add("nroPlano1", random(BigDecimal.class, range(1, 10)));
                add("nroPlano2", null);
                add("nroPlano3", null);
                add("nroPlano4", null);
                add("nroPlano5", null);
                add("nroPlano6", null);
                add("nroPlano7", null);
                add("nroPlano8", null);
                add("nroPlano9", null);
                add("nroPlano10", null);
                add("nroPlano11", null);
                add("nroPlano12", null);
                add("listaSalvar", l);
            }
        });

        Fixture.of(TaxaAdministrativa.class).addTemplate("valid", new Rule() {
            {
                add("codTaxaAdministrativa", random(Long.class, range(1, 200)));
                add("codEmp", random(Long.class, range(1, 200)));
                add("codOperadora", random(Long.class, range(1, 200)));
                add("codProduto", random(Long.class, range(1, 200)));
                add("nroPlano", random(Long.class, range(1, 15)));
                add("txAdmCadastrada", random(BigDecimal.class, range(1, 10)));
                add("codUsuario", random(Long.class, range(1, 200)));
                add("loja", one(PontoVendaCA.class, "valid"));
                add("operadora", one(OperadoraCA.class, "valid"));
                add("produto", one(ProdutoCA.class, "valid"));
            }
        });

        Fixture.of(LojaCA.class).addTemplate("valid", new Rule() {
            {
                add("nmeLoja", random("nmeLoja1", "nmeLoja2"));
            }
        });

        Fixture.of(PontoVendaCA.class).addTemplate("valid", new Rule() {
            {
                add("nmePontoVenda", random("nmePontoVenda1", "nmePontoVenda2"));
            }
        });

        Fixture.of(OperadoraCA.class).addTemplate("valid", new Rule() {
            {
                add("nmeOperadora", random("nmeOperadora1", "nmeOperadora2"));
            }
        });

        Fixture.of(ProdutoCA.class).addTemplate("valid", new Rule() {
            {
                add("nome", random("produto1", "produto2"));
            }
        });

    }
}

package br.com.accesstage.trustion.loader;

import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAntecipacaoDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAntecipacaoDTO;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;


public class TaxaAntecipacaoLoader implements TemplateLoader {

    @Override
    public void load() {

        Fixture.of(FiltroTaxaAntecipacaoDTO.class).addTemplate("valid", new Rule() {
            {
                add("idsEmp", random(Integer.class, range(1, 200)));
                add("dataInicial", random("20170101", "20171001"));
                add("dataFinal", random("20180101", "20181001"));
            }
        });

        Fixture.of(TaxaAntecipacaoDTO.class).addTemplate("valid", new Rule() {
            {
                add("produto", random("produto1", "produto2"));
                add("operadora", random("oper1", "oper2"));
                add("loja", random("loja1", "loja2"));
                add("plano", random("plano1", "plano2"));
                add("taxa", random(Double.class, range(1, 200)));
            }
        });

    }
}

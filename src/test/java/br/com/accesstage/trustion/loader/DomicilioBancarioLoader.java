package br.com.accesstage.trustion.loader;

import br.com.accesstage.trustion.asconciliacao.model.DomicilioBancario;
import br.com.accesstage.trustion.dto.FiltroDomicilioBancarioDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoDomicilioBancarioDTO;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;


public class DomicilioBancarioLoader implements TemplateLoader {

    @Override
    public void load() {

        Fixture.of(FiltroDomicilioBancarioDTO.class).addTemplate("valid", new Rule() {
            {
                add("codigoBancoStr", random("127", "341"));
                add("nroBanco", random(Integer.class, range(1, 200)));
                add("empID", random(Integer.class, range(1, 200)));
            }
        });

        Fixture.of(DomicilioBancario.class).addTemplate("valid", new Rule() {
            {
                add("descContaBanco", random("127", "341"));
                add("nroBanco", random(Integer.class, range(1, 200)));
                add("nroContaCorrente", random("127", "341"));
                add("nroAgencia", random(Integer.class, range(1, 200)));
                add("staAtivo", random("127", "341"));
                add("empID", random(Integer.class, range(1, 200)));
            }
        });

        Fixture.of(ResultadoDomicilioBancarioDTO.class).addTemplate("valid", new Rule() {
            {
                add("descContaBanco", random("127", "341"));
                add("nroBanco", random(Integer.class, range(1, 200)));
                add("nroContaCorrente", random("127", "341"));
                add("nroAgencia", random(Integer.class, range(1, 200)));
                add("staAtivo", random("127", "341"));
                add("empID", random(Integer.class, range(1, 200)));
            }
        });

    }
}

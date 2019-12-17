package br.com.accesstage.trustion.loader;

import br.com.accesstage.trustion.dto.TransportadoraDTO;
import br.com.accesstage.trustion.model.Transportadora;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import java.util.Date;

public class TransportadoraLoader implements TemplateLoader {

    @Override
    public void load() {

        Fixture.of(TransportadoraDTO.class).addTemplate("valid", new Rule() {
            {
                add("idTransportadora", random(Long.class, range(1, 200)));
                add("razaoSocial", random("razaoSocial1", "razaoSocial2"));
                add("cnpj", random("cnpj1", "cnpj2"));
                add("status", random("A", "I"));
            }
        });

        Fixture.of(Transportadora.class).addTemplate("valid", new Rule() {
            {
                add("idTransportadora", random(Long.class, range(1, 200)));
                add("razaoSocial", random("razaoSocial1", "razaoSocial2"));
                add("cnpj", random("cnpj1", "cnpj2"));
                add("endereco", random("endereco1", "endereco2"));
                add("cidade", random("cidade1", "cidade2"));
                add("estado", random("estado1", "estado2"));
                add("cep", random("cep1", "cep2"));
                add("dataCriacao", new Date());
                add("idUsuarioCriacao", random(Long.class, range(1, 200)));
                add("dataAlteracao", new Date());
                add("idUsuarioAlteracao", random(Long.class, range(1, 200)));
                add("nroTelefone", random("nroTelefone1", "nroTelefone2"));
                add("email", random("email1", "email2"));
                add("envioInformacao", random("envioInformacao1", "envioInformacao2"));
                add("status", random("A", "I"));
                add("responsavel", random("responsavel1", "responsavel2"));
            }
        });

    }
}

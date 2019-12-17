/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.accesstage.trustion.loader;


import br.com.accesstage.trustion.model.Ocorrencia;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import java.math.BigDecimal;


/**
 *
 * @author elaine.querido
 */
public class OcorrenciaLoader implements TemplateLoader {
    
    @Override
    public void load() {

        Fixture.of(Ocorrencia.class).addTemplate("valid_d1", new Rule() {
            {
                add("idOcorrencia", random(Long.class, range(1, 200)));
                add("idTipoQuestionamento", random(Long.class, range(1, 7)));
                add("valorQuestionado", random(BigDecimal.class, range(-100.00, 100.00)));
                //add("idStatusMescla", random(Long.class, range(-100.00, 100.00)));
                add("valorAjuste", random(BigDecimal.class, range(0.00, 100.00)));
                add("idTipoStatusOcorrencia", random(Long.class, range(1, 4)));
                add("dataStatusOcorrencia", instant("3 days ago"));
                add("idUsuarioCriacao", random(Long.class, range(2, 6)));
                add("dataCriacao", instant("10 days ago"));
                add("isOcorrenciaD1", true);
                //add("categoria", true);
            }
        })
         .addTemplate("valid_d0", new Rule() {
             {
                add("idOcorrencia", random(Long.class, range(1, 200)));
                add("idTipoQuestionamento", random(Long.class, range(1, 7)));
                add("valorQuestionado", random(BigDecimal.class, range(-100.00, 100.00)));
                //add("idStatusMescla", random(Long.class, range(-100.00, 100.00)));
                add("valorAjuste", random(BigDecimal.class, range(0.00, 100.00)));
                add("idTipoStatusOcorrencia", random(Long.class, range(1, 4)));
                add("dataStatusOcorrencia", instant("3 days ago"));
                add("idUsuarioCriacao", random(Long.class, range(2, 6)));
                add("dataCriacao", instant("10 days ago"));
                add("isOcorrenciaD1", false);
                    //add("categoria", true);
             }
        });
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.accesstage.trustion.loader;


import br.com.accesstage.trustion.model.Atividade;
import br.com.accesstage.trustion.model.Ocorrencia;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

/**
 *
 * @author elaine.querido
 */
public class AtividadeLoader implements TemplateLoader {
    
    @Override
    public void load() {

        Fixture.of(Atividade.class).addTemplate("valid_d1", new Rule() {
            {
                add("idAtividade", random(Long.class, range(1, 200)));
                add("idOcorrencia", random(Long.class, range(1, 200)));
            }
        });
    }
    
}

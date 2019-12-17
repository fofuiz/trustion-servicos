/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.accesstage.trustion.loader;


import br.com.accesstage.trustion.model.Usuario;
import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

/**
 *
 * @author elaine.querido
 */
public class UsuarioLoader  implements TemplateLoader {
    
    @Override
    public void load() {

        Fixture.of(Usuario.class).addTemplate("valid", new Rule() {
            {
                add("idUsuario", random(Long.class, range(2, 6)));
                add("login", random("teste.operador.transportadora", "teste", "teste.operador.cliente"));
                add("senha", random("xxx", "yyy"));
                add("nome", random("Teste1", "Teste2", "Teste3"));
                add("email", "${nome}@gmail.com");
                add("status", "A");
                add("primeiroAcesso", false);
                add("idPerfil", random(Long.class, range(1, 5)));
                add("dataCriacao", instant("5 days ago"));
                
            }
        });
    }
    
    
}

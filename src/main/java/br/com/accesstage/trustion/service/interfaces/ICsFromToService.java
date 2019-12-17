/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.fornax.model.CsFromTo;

/**
 *
 * @author elaine.querido
 */
public interface ICsFromToService {
    
    CsFromTo buscarValores(String codCliente, String tpDado);
    
}

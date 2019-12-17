/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.accesstage.trustion.service.impl;


import br.com.accesstage.trustion.fornax.model.CsFromTo;
import br.com.accesstage.trustion.fornax.repository.interfaces.CsFromToRepository;
import br.com.accesstage.trustion.service.interfaces.ICsFromToService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author caiomoraes
 */
@Service
public class CsFromToService  implements ICsFromToService {

    @Autowired
    private CsFromToRepository csFromToRepository;

    @Override
    public CsFromTo buscarValores(String codCliente, String tpDado) {
        CsFromTo csFromTo = null;
        List<CsFromTo> list = csFromToRepository.findAllByCodClienteAndTpDado(codCliente, tpDado);
        if(list != null && !list.isEmpty()){
            csFromTo = list.get(0);
        }
        
        return csFromTo;
    }
}

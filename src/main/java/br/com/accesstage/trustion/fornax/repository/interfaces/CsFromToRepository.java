/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.accesstage.trustion.fornax.repository.interfaces;


import br.com.accesstage.trustion.fornax.model.CsFromTo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CsFromToRepository extends JpaRepository<CsFromTo, Long>{
    
    List<CsFromTo> findAllByCodClienteAndTpDado(String codCliente, String tpDado);
    
}

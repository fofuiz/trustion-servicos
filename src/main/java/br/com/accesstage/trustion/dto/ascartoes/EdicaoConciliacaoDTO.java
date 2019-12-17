/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.accesstage.trustion.dto.ascartoes;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author elaine.querido
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdicaoConciliacaoDTO implements Serializable {

    private List<RemessaConciliacaoDetalheDTO> listarTransacoesASerConciliada;
    private List<RemessaConciliacaoDetalheDTO> listarPossiveisTransacoes;
    
    private List<RemessaConciliacaoDetalheDTO> listaTransacaoRemessa;
    private List<RemessaConciliacaoDetalheDTO> listaTransacaoFato;

    
    
    
    

}

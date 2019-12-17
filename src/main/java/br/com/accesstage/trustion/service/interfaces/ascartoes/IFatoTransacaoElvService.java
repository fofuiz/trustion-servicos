package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoElvCA;

/**
 *
 * @author raphael
 */
public interface IFatoTransacaoElvService {

    public FatoTransacaoElvCA listarHashElv(String hashValue);

    public FatoTransacaoElvCA merge(FatoTransacaoElvCA fatoTransacaoElv);
    
}

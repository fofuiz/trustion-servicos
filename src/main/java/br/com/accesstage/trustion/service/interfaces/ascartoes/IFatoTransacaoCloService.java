package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoCloCA;

/**
 *
 * @author raphael
 */
public interface IFatoTransacaoCloService {
    
    public FatoTransacaoCloCA listarHashClo(String hashValue);

    public FatoTransacaoCloCA merge(FatoTransacaoCloCA fatoTransacaoClo);
}

package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacao;

/**
 *
 * @author raphael
 */
public interface IFatoTransacaoService {

    public FatoTransacao listarHash(String hashValue);

    public FatoTransacao merge(FatoTransacao fatoTransacao);
    
}

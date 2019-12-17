package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoRdcCA;

/**
 *
 * @author raphael
 */
public interface IFatoTransacaoRdcService {

    public FatoTransacaoRdcCA listarHashRdc(String hashValue);

    public FatoTransacaoRdcCA merge(FatoTransacaoRdcCA fatoTransacaoRdc);
}

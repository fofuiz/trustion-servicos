package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoRdcCA;
import br.com.accesstage.trustion.repository.ascartoes.impl.FatoTransacaoRdcRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IFatoTransacaoRdcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raphael
 */
@Service
public class FatoTransacaoRdcService implements IFatoTransacaoRdcService {

    @Autowired
    private FatoTransacaoRdcRepository repository;

    @Override
    public FatoTransacaoRdcCA listarHashRdc(String hashValue) {
        return repository.listarHashRdc(hashValue);
    }

    @Override
    public FatoTransacaoRdcCA merge(FatoTransacaoRdcCA fatoTransacaoRdc) {
        return repository.merge(fatoTransacaoRdc);
    }
}

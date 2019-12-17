package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoElvCA;
import br.com.accesstage.trustion.repository.ascartoes.impl.FatoTransacaoElvRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IFatoTransacaoElvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raphael
 */
@Service
public class FatoTransacaoElvService implements IFatoTransacaoElvService {

    @Autowired
    private FatoTransacaoElvRepository repository;

    @Override
    public FatoTransacaoElvCA listarHashElv(String hashValue) {
        return repository.listarHashElv(hashValue);
    }

    @Override
    public FatoTransacaoElvCA merge(FatoTransacaoElvCA fatoTransacaoElv) {
        return repository.merge(fatoTransacaoElv);
    }
}

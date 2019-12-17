package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacaoCloCA;
import br.com.accesstage.trustion.repository.ascartoes.impl.FatoTransacaoCloRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IFatoTransacaoCloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raphael
 */
@Service
public class FatoTransacaoCloService implements IFatoTransacaoCloService {
    
    @Autowired
    private FatoTransacaoCloRepository fatoTransacaoCloDAO;

    @Override
    public FatoTransacaoCloCA listarHashClo(String hashValue) {
        return fatoTransacaoCloDAO.listarHashClo(hashValue);
    }

    @Override
    public FatoTransacaoCloCA merge(FatoTransacaoCloCA fatoTransacaoClo) {
        return fatoTransacaoCloDAO.merge(fatoTransacaoClo);
    }
}

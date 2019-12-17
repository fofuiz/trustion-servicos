package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.FatoTransacao;
import br.com.accesstage.trustion.repository.ascartoes.impl.FatoTransacaoRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IFatoTransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raphael
 */
@Service
public class FatoTransacaoService implements IFatoTransacaoService {
    
    @Autowired
    private FatoTransacaoRepository fatoTransacaoDAO;

    @Override
    public FatoTransacao listarHash(String hashValue) {
        return fatoTransacaoDAO.listarHash(hashValue);
    }

    @Override
    public FatoTransacao merge(FatoTransacao fatoTransacao) {
        return fatoTransacaoDAO.merge(fatoTransacao);
    }
}

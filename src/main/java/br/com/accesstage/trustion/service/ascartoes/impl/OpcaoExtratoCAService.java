package br.com.accesstage.trustion.service.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.OpcaoExtratoCA;
import br.com.accesstage.trustion.repository.ascartoes.interfaces.IOpcaoExtratoCARepository;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IOpcaoExtratoCAService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author raphael
 */
@Service
public class OpcaoExtratoCAService implements IOpcaoExtratoCAService {

    @Autowired
    private IOpcaoExtratoCARepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<OpcaoExtratoCA> carregarComboOpcaoExtrato() throws DataAccessException {
        return repository.findAllByOrderByCodigoOpcaoExtratoAsc();
    }
}

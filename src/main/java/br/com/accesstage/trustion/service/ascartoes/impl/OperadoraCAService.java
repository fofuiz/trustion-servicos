package br.com.accesstage.trustion.service.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.OperadoraCA;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.repository.ascartoes.interfaces.IOperadoraCARepository;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IOperadoraCAService;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperadoraCAService implements IOperadoraCAService {

    @Autowired
    private IOperadoraCARepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<OperadoraCA> carregarComboOperadora(boolean flgFiltroPortal) throws DataAccessException {
        return repository.findByFlgFiltroPortalOrderByNmeExibicaoPortalAsc(flgFiltroPortal);
    }

}

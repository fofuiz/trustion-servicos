package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.StatusConciliacaoCA;
import br.com.accesstage.trustion.repository.ascartoes.interfaces.IStatusConciliacaoRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IStatusConciliacaoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raphael
 */
@Service
public class StatusConciliacaoService implements IStatusConciliacaoService {

    @Autowired
    private IStatusConciliacaoRepository repository;

    @Override
    public List<StatusConciliacaoCA> listarTodos() {
        return repository.findAll();
    }
}

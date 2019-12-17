package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.VisaoGestaoVendasCA;
import br.com.accesstage.trustion.repository.ascartoes.interfaces.IVisaoGestaoVendasRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IVisaoGestaoVendasService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raphael
 */
@Service
public class VisaoGestaoVendasService implements IVisaoGestaoVendasService {

    @Autowired
    private IVisaoGestaoVendasRepository repository;

    @Override
    public List<VisaoGestaoVendasCA> listarTodos() {
        return repository.findAll();
    }
}

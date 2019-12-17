package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.dto.ascartoes.FiltroTransacoesNaoProcessadasDTO;
import br.com.accesstage.trustion.dto.ascartoes.TransacoesNaoProcessadasDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.TransacoesNaoProcessadasCARepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.ITransacoesNaoProcessadasService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raphael
 */
@Service
public class TransacoesNaoProcessadasService implements ITransacoesNaoProcessadasService {

    @Autowired
    private TransacoesNaoProcessadasCARepository repository;

    @Override
    public List<TransacoesNaoProcessadasDTO> buscarHistoricoTransacoesNaoProcessadas(FiltroTransacoesNaoProcessadasDTO filtro) {
        return repository.buscarHistoricoTransacoesNaoProcessadas(filtro);
    }

    @Override
    public List<TransacoesNaoProcessadasDTO> buscarHistoricoTransacoesNaoProcessadasAntecipacao(FiltroTransacoesNaoProcessadasDTO filtro) {
        return repository.buscarHistoricoTransacoesNaoProcessadasAntecipacao(filtro);
    }
}

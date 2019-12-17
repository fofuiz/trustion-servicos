package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.dto.ascartoes.BaixaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroBaixaDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.BaixaRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IBaixaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author raphael
 */
@Service
public class BaixaService implements IBaixaService {

    @Autowired
    private BaixaRepository baixaRepository;

    @Override
    public List<BaixaDTO> consultaConcParcela(FiltroBaixaDTO filtro) {
        return baixaRepository.consultaConcParcela(filtro);
    }

    @Override
    public List<BaixaDTO> consulta(FiltroBaixaDTO filtro) {
        return baixaRepository.consulta(filtro);
    }

    @Override
    public List<BaixaDTO> consultaLayout2(FiltroBaixaDTO filtro) {
        return baixaRepository.consultaLayout2(filtro);
    }

    @Override
    public List<BaixaDTO> consultaLayout2ConcParcela(FiltroBaixaDTO filtro) {
        return baixaRepository.consultaLayout2ConcParcela(filtro);
    }

    @Override
    public List<BaixaDTO> consultaLayoutCSV(FiltroBaixaDTO filtro) {
        return baixaRepository.consultaLayoutCSV(filtro);
    }

    @Override
    public List<BaixaDTO> consultaLayoutCSVConcParcela(FiltroBaixaDTO filtro) {
        return baixaRepository.consultaLayoutCSVConcParcela(filtro);
    }
}

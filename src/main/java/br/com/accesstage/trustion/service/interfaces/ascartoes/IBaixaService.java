package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.dto.ascartoes.BaixaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroBaixaDTO;
import java.util.List;

/**
 *
 * @author raphael
 */
public interface IBaixaService {

    List<BaixaDTO> consultaConcParcela(FiltroBaixaDTO filtro);

    List<BaixaDTO> consulta(FiltroBaixaDTO filtro);

    List<BaixaDTO> consultaLayout2(FiltroBaixaDTO filtro);

    List<BaixaDTO> consultaLayout2ConcParcela(FiltroBaixaDTO filtro);

    List<BaixaDTO> consultaLayoutCSV(FiltroBaixaDTO filtro);

    List<BaixaDTO> consultaLayoutCSVConcParcela(FiltroBaixaDTO filtro);
}

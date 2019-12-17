package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.dto.ascartoes.FiltroGestaoVendaDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.dto.ascartoes.SemaforoDTO;
import java.util.List;

/**
 *
 * @author raphael
 */
public interface IGestaoVendasService {

    List<GestaoVendasDTO> pesquisar(FiltroGestaoVendaDTO filtroGestaoVendaDTO) throws Exception;

    List<GestaoVendasDTO> pesquisarSemaforo(FiltroGestaoVendaDTO filtroGestaoVendaDTO) throws Exception;

    List<GestaoVendasDTO> consultaBandeira(FiltroGestaoVendaDTO filtro);

    List<GestaoVendasDTO> buscarDivergenciaLoja(FiltroGestaoVendaDTO filtro);

    List<SemaforoDTO> carregarSemaforoZerado();

}

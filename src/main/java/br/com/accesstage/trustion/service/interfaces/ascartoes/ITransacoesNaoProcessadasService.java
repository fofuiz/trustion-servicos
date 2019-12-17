package br.com.accesstage.trustion.service.interfaces.ascartoes;

import br.com.accesstage.trustion.dto.ascartoes.FiltroTransacoesNaoProcessadasDTO;
import br.com.accesstage.trustion.dto.ascartoes.TransacoesNaoProcessadasDTO;
import java.util.List;

/**
 *
 * @author raphael
 */
public interface ITransacoesNaoProcessadasService {

    public List<TransacoesNaoProcessadasDTO> buscarHistoricoTransacoesNaoProcessadas(
            FiltroTransacoesNaoProcessadasDTO filtro);

    public List<TransacoesNaoProcessadasDTO> buscarHistoricoTransacoesNaoProcessadasAntecipacao(
            FiltroTransacoesNaoProcessadasDTO filtro);
}

package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.DetalheConferenciaDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoD1DTO;
import java.util.List;

public interface IDetalheConferenciaService {

    List<DetalheConferenciaDTO> pesquisarDetalheConferencia(RelatorioAnaliticoCreditoD1DTO relatorioAnaliticoCreditoD1DTO) throws Exception;
}

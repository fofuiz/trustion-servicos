package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoTotalDTO;
import br.com.accesstage.trustion.enums.StatusConciliacaoEnum;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRelatorioAnaliticoCreditoService {

    Page<RelatorioAnaliticoCreditoDTO> listarCriterios(RelatorioAnaliticoCreditoDTO relAnaliticoCredDTO, Pageable pageable) throws Exception;

    RelatorioAnaliticoCreditoDTO pesquisar(Long id) throws Exception;

    RelatorioAnaliticoCreditoDTO pesquisarPorIdConciliacao(Long idConciliacao) throws Exception;

    RelatorioAnaliticoCreditoDTO alterarStatusConciliacao(RelatorioAnaliticoCreditoDTO relatorioAnaliticoCreditoDTO, StatusConciliacaoEnum statusConciliacaoEnum);

    RelatorioAnaliticoTotalDTO calcularRelatorioTotal7Dias() throws Exception;

    RelatorioAnaliticoTotalDTO calcularRelatorioTotalPeriodoUsuario() throws Exception;

    RelatorioAnaliticoTotalDTO calcularRelatorioTotalPeriodo(Date dataInicial, Date dataFinal) throws Exception;

    RelatorioAnaliticoCreditoDTO pesquisarPorIdOcorrencia(Long idOcorrencia) throws Exception;
}

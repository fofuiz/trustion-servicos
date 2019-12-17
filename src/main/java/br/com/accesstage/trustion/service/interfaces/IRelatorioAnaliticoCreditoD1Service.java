package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.OcorrenciaD1DTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoD1DTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoTotalDTO;
import br.com.accesstage.trustion.enums.StatusConciliacaoEnum;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRelatorioAnaliticoCreditoD1Service {

    Page<RelatorioAnaliticoCreditoD1DTO> listarCriterios(RelatorioAnaliticoCreditoD1DTO relAnaliticoCredDTO, Pageable pageable) throws Exception;

    RelatorioAnaliticoCreditoD1DTO alterarStatusOcorrencia(OcorrenciaD1DTO dto) throws Exception;

    RelatorioAnaliticoCreditoD1DTO atualizarStatusOcorrencia(OcorrenciaD1DTO dto) throws Exception;

    RelatorioAnaliticoCreditoD1DTO pesquisar(Long id) throws Exception;

    RelatorioAnaliticoCreditoD1DTO pesquisarPorIdConciliacao(Long idConciliacao) throws Exception;

    RelatorioAnaliticoCreditoD1DTO alterarStatusConciliacao(RelatorioAnaliticoCreditoD1DTO relatorioAnaliticoCreditoD1DTO, StatusConciliacaoEnum statusConciliacaoEnum);

    RelatorioAnaliticoTotalDTO calcularRelatorioTotal7Dias() throws Exception;

    RelatorioAnaliticoTotalDTO calcularRelatorioTotalPeriodoUsuario() throws Exception;

    RelatorioAnaliticoTotalDTO calcularRelatorioTotalPeriodo(Date dataInicial, Date dataFinal) throws Exception;
}

package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.OcorrenciaD1DTO;
import br.com.accesstage.trustion.dto.RelatorioOcorrenciaDNDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOcorrenciaD1Service {

    OcorrenciaD1DTO criar(OcorrenciaD1DTO dto) throws Exception;

    OcorrenciaD1DTO alterar(OcorrenciaD1DTO dto) throws Exception;

    OcorrenciaD1DTO pesquisar(Long idOcorrencia) throws Exception;

    List<OcorrenciaD1DTO> listarTodos() throws Exception;

    OcorrenciaD1DTO alterarStatus(OcorrenciaD1DTO dto) throws Exception;

    Page<RelatorioOcorrenciaDNDTO> listarPorCriterio(RelatorioOcorrenciaDNDTO dto, Pageable pageable) throws Exception;

    List<RelatorioOcorrenciaDNDTO> listarPorCriterioExportar(RelatorioOcorrenciaDNDTO dto);

}

package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.OcorrenciaDTO;
import br.com.accesstage.trustion.dto.RelatorioOcorrenciaDTO;
import br.com.accesstage.trustion.model.Ocorrencia;
import br.com.accesstage.trustion.model.Usuario;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOcorrenciaService {

    OcorrenciaDTO criar(OcorrenciaDTO dto) throws Exception;

    OcorrenciaDTO alterar(OcorrenciaDTO dto) throws Exception;

    OcorrenciaDTO pesquisar(Long idOcorrencia) throws Exception;

    List<OcorrenciaDTO> listarTodos() throws Exception;

    OcorrenciaDTO alterarStatus(OcorrenciaDTO dto) throws Exception;

    OcorrenciaDTO mesclar(OcorrenciaDTO dto) throws Exception;

    OcorrenciaDTO desmesclar(OcorrenciaDTO dto) throws Exception;

    List<OcorrenciaDTO> listarMesclados(Long idOcorrencia) throws Exception;

    OcorrenciaDTO aprovarMescla(Long idOcorrencia) throws Exception;

    OcorrenciaDTO rejeitarMescla(Long idOcorrencia);

    List<OcorrenciaDTO> listarParaMescla(Long idOcorrencia) throws Exception;

    Page<RelatorioOcorrenciaDTO> listarPorCriterio(RelatorioOcorrenciaDTO dto, Pageable pageable) throws Exception;

    List<RelatorioOcorrenciaDTO> listarPorCriterioExportar(RelatorioOcorrenciaDTO dto);
    
    boolean verificarPermissaoPorUsuario(Usuario usuarioSolicitante, Usuario usuarioLogado);
    
    String getResponsavel(Ocorrencia ocorrencia);
    
    String getResponsavelAprovacao(Ocorrencia ocorrencia);

}

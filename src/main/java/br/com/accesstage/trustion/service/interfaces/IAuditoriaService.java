package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.AuditoriaDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuditoriaService {

    List<AuditoriaDTO> listarPorUsuario(AuditoriaDTO dto) throws Exception;

    Page<AuditoriaDTO> listarPorUsuario(AuditoriaDTO dto, Pageable pageable) throws Exception;

    AuditoriaDTO registrar(String acao, Long idOcorrencia) throws Exception;
}

package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.TransportadoraDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITransportadoraService {

    TransportadoraDTO criar(TransportadoraDTO dto) throws Exception;

    TransportadoraDTO alterar(TransportadoraDTO dto) throws Exception;

    boolean excluir(Long id) throws Exception;

    List<TransportadoraDTO> listarTodos() throws Exception;

    TransportadoraDTO pesquisar(Long id) throws Exception;

    Page<TransportadoraDTO> listarCriterios(TransportadoraDTO dto, Pageable pageable) throws Exception;

    List<TransportadoraDTO> listarCriterios(TransportadoraDTO dto) throws Exception;
    
    List<TransportadoraDTO> listarPorPerfilUsuarioETipoCredito(Long idPerfil, Long tipoCredito) throws Exception;
}

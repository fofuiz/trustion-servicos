package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.accesstage.trustion.dto.NotificacaoDTO;

public interface INotificacaoService {

    NotificacaoDTO criar(NotificacaoDTO notificacaoDTO) throws Exception;

    NotificacaoDTO alterar(NotificacaoDTO notificacaoDTO) throws Exception;

    NotificacaoDTO pesquisar(Long idNotificacaoDTO) throws Exception;

    void excluir(Long idUsuario) throws Exception;

    List<NotificacaoDTO> listarCriterios(NotificacaoDTO notificacaoDTO) throws Exception;

    Page<NotificacaoDTO> listarCriterios(NotificacaoDTO notificacaoDTO, Pageable pageable) throws Exception;

    void enviarNotificacaoAnaliseDivergencia(Long idOcorrencia, Long idEmpresa) throws Exception;

    List<NotificacaoDTO> pesquisarByUsuario(Long idUsuario) throws Exception;

    List<NotificacaoDTO> pesquisarByUsuarioAndIdGrupoEmpresa(Long idUsuario, Long idGrupoEmpresa) throws Exception;

    List<NotificacaoDTO> pesquisarByUsuarioAndIdTransportadora(Long idUsuario, Long idTransportadora) throws Exception;
}

package br.com.accesstage.trustion.service.interfaces;

import java.util.List;

import br.com.accesstage.trustion.dto.NotificacaoDTO;

public interface INotificacao {
	NotificacaoDTO criar(NotificacaoDTO notificacaoDTO) throws Exception;
	NotificacaoDTO alterar(NotificacaoDTO notificacaoDTO) throws Exception;
	NotificacaoDTO pesquisar(Long idNotificacaoDTO) throws Exception;
	boolean excluir(Long notificacaoDTO) throws Exception;
	List<NotificacaoDTO> listarCriterios(NotificacaoDTO notificacaoDTO)throws Exception;
}

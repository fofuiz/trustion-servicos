package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.NotificacaoDTO;
import br.com.accesstage.trustion.model.Notificacao;

public class NotificacaoConverter {

    public static Notificacao paraEntidade(NotificacaoDTO notificacaoDTO) {

        Notificacao notificacao = new Notificacao();

        notificacao.setIdNotificacao(notificacaoDTO.getIdNotificacao());
        notificacao.setIdGrupoEconomico(notificacaoDTO.getIdGrupoEconomico());
        notificacao.setIdEmpresa(notificacaoDTO.getIdEmpresa());
        notificacao.setIdTipoNotificacao(notificacaoDTO.getIdTipoNotificacao());
        notificacao.setUsuario(UsuarioConverter.paraEntidadeComID(notificacaoDTO.getUsuarioDTO()));
        notificacao.setDataCriacao(notificacaoDTO.getDataCriacao());
        notificacao.setIdUsuarioCriacao(notificacaoDTO.getIdUsuarioCriacao());
        notificacao.setIdUsuarioAlteracao(notificacaoDTO.getIdUsuarioAlteracao());
        notificacao.setDataAlteracao(notificacaoDTO.getDataAlteracao());
        notificacao.setStatus(notificacaoDTO.getStatus());
        notificacao.setIdTransportadora(notificacaoDTO.getIdTransportadora());

        return notificacao;

    }

    public static NotificacaoDTO paraDTO(Notificacao notificacao) {

        NotificacaoDTO notificacaoDTO = new NotificacaoDTO();

        notificacaoDTO.setIdNotificacao(notificacao.getIdNotificacao());
        notificacaoDTO.setIdGrupoEconomico(notificacao.getIdGrupoEconomico());
        notificacaoDTO.setIdEmpresa(notificacao.getIdEmpresa());
        notificacaoDTO.setIdTipoNotificacao(notificacao.getIdTipoNotificacao());
        notificacaoDTO.setUsuarioDTO(UsuarioConverter.paraDTO(notificacao.getUsuario()));
        notificacaoDTO.setDataCriacao(notificacao.getDataCriacao());
        notificacaoDTO.setIdUsuarioCriacao(notificacao.getIdUsuarioCriacao());
        notificacaoDTO.setIdUsuarioAlteracao(notificacao.getIdUsuarioAlteracao());
        notificacaoDTO.setDataAlteracao(notificacao.getDataAlteracao());
        notificacaoDTO.setStatus(notificacao.getStatus());
        notificacaoDTO.setIdTransportadora(notificacao.getIdTransportadora());

        return notificacaoDTO;
    }
}

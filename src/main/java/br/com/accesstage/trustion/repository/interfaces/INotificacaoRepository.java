package br.com.accesstage.trustion.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.Notificacao;
import br.com.accesstage.trustion.model.Usuario;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface INotificacaoRepository extends JpaRepository<Notificacao, Long>, JpaSpecificationExecutor<Notificacao> {

    List<Notificacao> findByIdTipoNotificacaoAndIdEmpresaAndStatus(Long idTipoNotificacao, Long idEmpresa, String status);

    @Transactional
    void deleteByUsuario(Usuario usuario);

    List<Notificacao> findDistinctByUsuarioOrderByIdNotificacao(Usuario usuario);

    List<Notificacao> findDistinctByUsuarioAndIdGrupoEconomicoOrderByIdNotificacao(Usuario usuario, Long idGrupoEconomico);

    List<Notificacao> findDistinctByUsuarioAndIdTransportadoraOrderByIdNotificacao(Usuario usuario, Long idTransportadora);
}

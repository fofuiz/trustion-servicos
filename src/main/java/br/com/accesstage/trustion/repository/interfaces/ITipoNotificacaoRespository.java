package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.TipoNotificacao;

@Repository
public interface ITipoNotificacaoRespository extends JpaRepository<TipoNotificacao,Long> {

}

package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.accesstage.trustion.model.TipoNotificacao;

public interface ITipoNotificacao extends JpaRepository<TipoNotificacao,Long> {

}

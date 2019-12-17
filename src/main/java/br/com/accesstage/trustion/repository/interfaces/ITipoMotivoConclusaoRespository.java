package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.TipoMotivoConclusao;

import java.util.List;

@Repository
public interface ITipoMotivoConclusaoRespository extends JpaRepository<TipoMotivoConclusao, Long>{

	TipoMotivoConclusao findByDescricao(String descricao);

	List<TipoMotivoConclusao> findByDescricaoContaining(String descricao);

}

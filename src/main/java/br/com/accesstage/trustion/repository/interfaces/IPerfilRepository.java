package br.com.accesstage.trustion.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.accesstage.trustion.model.Perfil;

public interface IPerfilRepository extends JpaRepository<Perfil, Long> {

	Perfil findByDescricao(String descricao);

	List<Perfil> findByIdPerfilIn(List<Long> idsPerfil);
}

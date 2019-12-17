package br.com.accesstage.trustion.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

	boolean existsByLogin(String login);

	boolean existsByEmail(String email);

	Usuario findOneByLogin(String login);

	Usuario findByIdUsuario(Long idUsuario);

	List<Usuario> findByEmpresaList_IdEmpresa(Long idEmpresa);
	
	List<Usuario> findByEmpresaList_IdGrupoEconomico(Long idGrupoEconomico);

}

package br.com.accesstage.trustion.repository.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.Parametros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author raphael
 */
@Repository
public interface IParametrosRepository extends JpaRepository<Parametros, Long> {
}

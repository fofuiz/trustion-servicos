package br.com.accesstage.trustion.repository.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.VisaoGestaoVendasCA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author raphael
 */
@Repository
public interface IVisaoGestaoVendasRepository extends JpaRepository<VisaoGestaoVendasCA, Long> {
}

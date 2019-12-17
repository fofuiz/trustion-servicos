package br.com.accesstage.trustion.repository.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.OpcaoExtratoCA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author raphael
 */
@Repository
public interface IOpcaoExtratoCARepository extends JpaRepository<OpcaoExtratoCA, Long> {

    List<OpcaoExtratoCA> findAllByOrderByCodigoOpcaoExtratoAsc();

}

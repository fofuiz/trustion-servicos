package br.com.accesstage.trustion.repository.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.OperadoraCA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOperadoraCARepository extends JpaRepository<OperadoraCA, Long> {

    List<OperadoraCA> findByFlgFiltroPortalOrderByNmeExibicaoPortalAsc(boolean flgFiltroPortal);


}

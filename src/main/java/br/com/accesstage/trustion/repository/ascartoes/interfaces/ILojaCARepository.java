package br.com.accesstage.trustion.repository.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.LojaCA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILojaCARepository extends JpaRepository<LojaCA, Long> {

    List<LojaCA> findByIdEmpresaOrderByNomeAsc(Long empid);

}

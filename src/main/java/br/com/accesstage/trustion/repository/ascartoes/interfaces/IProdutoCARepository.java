package br.com.accesstage.trustion.repository.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.ProdutoCA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProdutoCARepository extends JpaRepository<ProdutoCA, Long> {

    List<ProdutoCA> findByIdNotAndPermiteSelecionarAndNomeIsNotNullOrderByNomeAsc(Long id, Integer permiteSelecionar);

}

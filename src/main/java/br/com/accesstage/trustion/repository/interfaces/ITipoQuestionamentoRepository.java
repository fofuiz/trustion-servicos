package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.TipoQuestionamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoQuestionamentoRepository extends JpaRepository<TipoQuestionamento, Long> {
    List<TipoQuestionamento> findByDescricaoContaining(String descricao);
}

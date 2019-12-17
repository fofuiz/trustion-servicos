package br.com.accesstage.trustion.repository.ascartoes.interfaces;

import br.com.accesstage.trustion.ascartoes.model.ProdutoOperadoraCA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProdutoOperadoraCARepository extends JpaRepository<ProdutoOperadoraCA, Long> {

}

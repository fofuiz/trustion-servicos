package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.ListaBanco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IListaBancoRepository extends JpaRepository<ListaBanco, Long>{

    ListaBanco findOneByCodigoBanco(String codigoBanco);

}

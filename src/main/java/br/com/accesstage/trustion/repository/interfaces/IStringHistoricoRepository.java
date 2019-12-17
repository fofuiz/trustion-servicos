package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.StringHistorico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IStringHistoricoRepository extends JpaRepository<StringHistorico, Long>, JpaSpecificationExecutor<StringHistorico> {

    boolean existsByIdListaBancoAndTextoIgnoringCase(Long idListaBanco, String texto);
}

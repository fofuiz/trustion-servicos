package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.ConciliacaoVendasPac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IConciliacaoVendasPac extends JpaRepository<ConciliacaoVendasPac, Long>, JpaSpecificationExecutor<ConciliacaoVendasPac> {

}

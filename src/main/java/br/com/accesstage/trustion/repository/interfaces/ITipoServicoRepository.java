package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.TipoServico;

@Repository
public interface ITipoServicoRepository extends JpaRepository<TipoServico,Long>,JpaSpecificationExecutor<TipoServico>{

}

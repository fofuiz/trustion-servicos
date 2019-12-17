package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.RelatorioOcorrenciaD0;

@Repository
public interface IRelatorioOcorrenciaRepository extends JpaRepository<RelatorioOcorrenciaD0, Long>,JpaSpecificationExecutor<RelatorioOcorrenciaD0> {
	
}

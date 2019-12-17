package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.RelatorioOcorrenciaDN;

@Repository
public interface IRelatorioOcorrenciaDnRepository extends JpaRepository<RelatorioOcorrenciaDN, Long>, JpaSpecificationExecutor<RelatorioOcorrenciaDN> {

}

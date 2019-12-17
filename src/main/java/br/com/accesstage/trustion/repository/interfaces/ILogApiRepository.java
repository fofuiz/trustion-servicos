package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.LogApi;

@Repository
public interface ILogApiRepository extends JpaRepository<LogApi, Long>, JpaSpecificationExecutor<LogApi>{

}

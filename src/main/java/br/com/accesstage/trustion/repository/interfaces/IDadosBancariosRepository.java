package br.com.accesstage.trustion.repository.interfaces;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.accesstage.trustion.model.DadosBancarios;

public interface IDadosBancariosRepository extends JpaRepository<DadosBancarios, Long>, JpaSpecificationExecutor<DadosBancarios>{
		List<DadosBancarios> findAllByIdEmpresa(Long idEmpresa);
		
		

}

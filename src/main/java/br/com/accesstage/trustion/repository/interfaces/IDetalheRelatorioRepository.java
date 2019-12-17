package br.com.accesstage.trustion.repository.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import br.com.accesstage.trustion.model.DetalheRelatorio;

@Repository
public interface IDetalheRelatorioRepository extends JpaRepository<DetalheRelatorio, Long>{

	List<DetalheRelatorio> findAllByCodigoFechamento(Long codigoFechamento);
	
	@Query("select a from t_detalhe_relatorio a where a.codigoFechamento = ?1 and numSerie = ?2")
	Page<DetalheRelatorio> findAllByCodigoFechamento(Long codigoFechamento, String numSerie, Pageable pageable);

}

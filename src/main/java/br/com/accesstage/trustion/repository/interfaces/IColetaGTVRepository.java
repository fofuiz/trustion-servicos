package br.com.accesstage.trustion.repository.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.ColetaGTV;


@Repository
public interface IColetaGTVRepository extends JpaRepository<ColetaGTV, Long>,JpaSpecificationExecutor<ColetaGTV>{
	List<ColetaGTV> findByNumSerieAndIdEquipamentoAndPeriodoInicialDtAndColetaDt(String numSerie,Long idEquipamento,LocalDateTime periodoInicialDt,LocalDateTime coletaDt);
	
	Page<ColetaGTV> findByNumSerieAndIdEquipamento(String numSerie,Long idEquipamento, Specification<ColetaGTV> specs,Pageable pageable);
}

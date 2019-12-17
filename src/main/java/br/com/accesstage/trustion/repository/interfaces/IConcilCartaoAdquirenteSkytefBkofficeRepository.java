package br.com.accesstage.trustion.repository.interfaces;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.ConcilCartaoAdquirenteSkytefBkoffice;

@Repository
public interface IConcilCartaoAdquirenteSkytefBkofficeRepository extends JpaRepository<ConcilCartaoAdquirenteSkytefBkoffice, Long> {

	@Query(value = "from ConcilCartaoAdquirenteSkytefBkofficeEntity concil"
			+ " where dataConcil BETWEEN :startDate AND :endDate")
	public Page<ConcilCartaoAdquirenteSkytefBkoffice> findAllRangeDate(
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate, 
			Pageable paginacao);

	public ConcilCartaoAdquirenteSkytefBkoffice findByDataConcil(LocalDate startDate);

}

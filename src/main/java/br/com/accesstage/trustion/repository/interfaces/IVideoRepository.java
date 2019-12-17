package br.com.accesstage.trustion.repository.interfaces;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.VideoGTV;

@Repository
public interface IVideoRepository extends JpaRepository<VideoGTV, Long> {

	//@Query(value = "SELECT v FROM VideoGTVEntity v WHERE (v.dataVideoGtv >= :startDate AND v.dataVideoGtv <= :endDate)")
	@Query(value = "SELECT v FROM VideoGTVEntity v WHERE (v.dataVideoGtv BETWEEN :startDate AND :endDate)"
			+ " AND v.gtv = :gtv")
	Page<VideoGTV> findAllWithFiltersWithJPA(
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate, 
			@Param("gtv") Long gtv,
			Pageable paginacao);

	
}

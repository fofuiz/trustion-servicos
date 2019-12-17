package br.com.accesstage.trustion.repository.interfaces;

import java.util.Date;
import java.util.List;

import br.com.accesstage.trustion.dto.BkOfficeEntityDTO;
import br.com.accesstage.trustion.modelteste.Sprint05Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.BkOfficeDetalhe;
import br.com.accesstage.trustion.model.MovimentoDiario;

@Repository
public interface IRelatorioConcilCartoesRepository extends JpaRepository<MovimentoDiario, Long> {

	@Query( "SELECT "
			+ " new br.com.accesstage.trustion.model.BkOfficeDetalhe(m.bandeira, SUM(m.valor) AS total) "
			+ " FROM MovimentoDiarioEntity m"
			+ " WHERE m.dataMovimentoDiario = :data"
			+ " GROUP BY m.bandeira")
	public List<BkOfficeDetalhe> retornarTotalBandeiras(@Param("data") Date data);

}

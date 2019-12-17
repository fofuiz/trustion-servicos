package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.MovimentoDiario;

import java.util.Date;
import java.util.List;

@Repository
public interface IVendasRepository extends JpaRepository<MovimentoDiario, String> {

    List<MovimentoDiario> findAllByDataMovimentoDiarioGreaterThanEqualAndDataMovimentoDiarioLessThanEqual(Date startDate, Date endDate);

}

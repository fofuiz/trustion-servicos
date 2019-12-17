package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.CalendarioColetaNumerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ICalendarioColetaNumerarioRepository extends JpaRepository<CalendarioColetaNumerario,Long>, JpaSpecificationExecutor<CalendarioColetaNumerario> {

//    @Query(value = "SELECT u FROM t_calend_col_numerario u WHERE monday = '1'")
//    public List<CalendarioColetaNumerario> consultaPorDiasDaSemana(String diaSemana);


}


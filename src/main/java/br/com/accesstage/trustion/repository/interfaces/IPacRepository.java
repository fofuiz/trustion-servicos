package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.Pac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IPacRepository extends JpaRepository<Pac, Long> {
    List<Pac> findAllByDataColetaGreaterThanEqualAndDataColetaLessThanEqual(Date startDate, Date endDate);
}

package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.PeriodoResumoNumerario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPeriodoResumoNumerarioRepository extends JpaRepository<PeriodoResumoNumerario, Long> {

    boolean existsByIdUsuario(Long idUsuario);

    PeriodoResumoNumerario findOneByIdUsuario(Long idUsuario);

}

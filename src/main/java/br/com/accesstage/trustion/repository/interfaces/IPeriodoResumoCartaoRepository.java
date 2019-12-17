package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.PeriodoResumoCartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPeriodoResumoCartaoRepository extends JpaRepository<PeriodoResumoCartao, Long> {

    boolean existsByIdUsuario(Long idUsuario);

    PeriodoResumoCartao findOneByIdUsuario(Long idUsuario);

}

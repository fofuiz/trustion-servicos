package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.ExtratoElegivel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExtratoElegivelRepository extends JpaRepository<ExtratoElegivel, Long>, JpaSpecificationExecutor<ExtratoElegivel>{

    List<ExtratoElegivel> findAllByIdConciliacao(Long idConciliacao);

    List<ExtratoElegivel> findAllByIdExtElegivelIn(List<Long> idsExtElegiveis);
}

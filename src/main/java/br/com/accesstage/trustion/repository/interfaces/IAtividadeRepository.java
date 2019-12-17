package br.com.accesstage.trustion.repository.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.Atividade;

@Repository
public interface IAtividadeRepository extends JpaRepository<Atividade, Long> {

	List<Atividade> findAllByIdOcorrenciaOrderByDataHorarioDesc(Long idOcorrencia);
	Page<Atividade> findAllByIdOcorrenciaOrderByDataHorarioDesc(Long idOcorrencia, Pageable pageable);
        
        
}

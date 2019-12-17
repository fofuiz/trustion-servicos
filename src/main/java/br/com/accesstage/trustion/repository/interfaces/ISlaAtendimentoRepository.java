package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.SlaAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ISlaAtendimentoRepository extends JpaRepository<SlaAtendimento, Long>, JpaSpecificationExecutor<SlaAtendimento> {

    SlaAtendimento findByIdModeloNegocio(Long id);

}

package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.GrupoEconomico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IGrupoEconomicoRepository
        extends JpaRepository<GrupoEconomico, Long>, JpaSpecificationExecutor<GrupoEconomico> {

    boolean existsByCnpj(String cnpj);

    List<GrupoEconomico> findAllByIdUsuarioCriacao(Long idUsuarioCriacao);

    GrupoEconomico findByNome(String nome);

}

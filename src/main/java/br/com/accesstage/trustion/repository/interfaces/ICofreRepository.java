package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.Cofre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ICofreRepository extends JpaRepository<Cofre, Long>, JpaSpecificationExecutor<Cofre> {

    boolean existsByIdEquipamento(Long idEquipamento);

    boolean existsByNumSerie(String numSerie);
    
    Cofre findOneByIdEquipamentoAndNumSerie(Long idEquipamento, String numSerie);

    List<Cofre> findAllByIdEmpresa(Long idEmpresa);
}

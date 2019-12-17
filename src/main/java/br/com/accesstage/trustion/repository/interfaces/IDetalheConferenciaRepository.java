package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.DetalheConferencia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetalheConferenciaRepository extends JpaRepository<DetalheConferencia, Long> {

    List<DetalheConferencia> findByNumeroGVT(String numeroGVT);
}

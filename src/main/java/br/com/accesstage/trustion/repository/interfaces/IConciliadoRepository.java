package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.Conciliado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConciliadoRepository extends JpaRepository<Conciliado, Long> {

}

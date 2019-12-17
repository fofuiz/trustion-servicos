package br.com.accesstage.trustion.repository.ascartoes.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.ascartoes.model.TaxaAdministrativa;

@Repository
public interface ITaxaAdministrativaRepository extends JpaRepository<TaxaAdministrativa, Long> {

}

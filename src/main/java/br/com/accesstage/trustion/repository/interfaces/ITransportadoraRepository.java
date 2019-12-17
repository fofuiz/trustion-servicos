package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.Transportadora;

@Repository
public interface ITransportadoraRepository extends JpaRepository<Transportadora, Long>, JpaSpecificationExecutor<Transportadora> {

    boolean existsByCnpj(String cnpj);

    Transportadora findOneByCnpj(String cnpj);

    Transportadora findByIdTransportadora(Long idTransportadora);

}

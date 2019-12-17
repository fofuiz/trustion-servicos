package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.ModeloNegocio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IModeloNegocioRepository extends JpaRepository<ModeloNegocio, Long>, JpaSpecificationExecutor<ModeloNegocio> {

    List<ModeloNegocio> findAllByidTipoCredito(Long idTipoCredito);

    ModeloNegocio findAllByidTipoCreditoAndIdModeloNegocioIn(Long idTipoCredito, List<Long> idModeloNegocio);

    boolean existsByIdModeloNegocioAndIdTipoCredito(Long idModeloNegocio, Long idTipoCredito);

    List<ModeloNegocio> findAllByEmpresaModeloNegocioCollection_empresa_idEmpresaAndIdTipoCredito(Long idEmpresa, Long idTipoCredito);

    List<ModeloNegocio> findByIdTransportadora(Long idTransportadora);
    
    List<ModeloNegocio> findDistinctAllByEmpresaModeloNegocioCollection_empresa_idEmpresaIn(List<Long> idEmpresa);
}

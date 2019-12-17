package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.EmpresaModeloNegocio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmpresaModeloNegocioRepository extends JpaRepository<EmpresaModeloNegocio, Long>, JpaSpecificationExecutor<EmpresaModeloNegocio> {

    List<EmpresaModeloNegocio> findByEmpresa(Empresa empresa);
    
    List<EmpresaModeloNegocio> findByEmpresa_IdEmpresaAndModeloNegocio_idTipoCreditoAndModeloNegocio_transportadora_status(Long idEmpresa, Long idTipoCredito, String statusTransportadora);
    
    List<EmpresaModeloNegocio> findDistinctModeloNegocioByModeloNegocio_idTipoCreditoAndModeloNegocio_transportadora_status(Long idTipoCredito, String statusTransportadora);
}

package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.Empresa;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmpresaRepository extends JpaRepository<Empresa, Long>, JpaSpecificationExecutor<Empresa> {

    List<Empresa> findAllByIdGrupoEconomicoIn(List<Long> idGrupoEconomico);

    List<Empresa> findDistinctByIdGrupoEconomicoInAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(List<Long> idGrupoEconomico, List<Long> idModeloNegocio);

    List<Empresa> findDistinctByIdGrupoEconomicoInAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioInAndStatus(List<Long> idGrupoEconomico, List<Long> idModeloNegocio, String status);
    
    List<Empresa> findDistinctByIdGrupoEconomicoInAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioInAndStatusOrderByRazaoSocial(List<Long> idGrupoEconomico, List<Long> idModeloNegocio, String status);

    List<Empresa> findDistinctByIdGrupoEconomicoInAndStatusOrderByRazaoSocial(List<Long> idGrupoEconomico, String status);

    List<Empresa> findDistinctByEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(List<Long> idModeloNegocio);

    List<Empresa> findAllByCnpjAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(String cnpj, List<Long> idModeloNegocio);

    Empresa findByCnpjAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocio(String cnpj, Long idModeloNegocio);

    List<Empresa> findAllByCnpj(String cnpj);

    Empresa findOneByCnpjAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(String cnpj, List<Long> idModeloNegocio);

    Empresa findOneByIdEmpresaAndStatus(Long idEmpresa, String status);

    boolean existsByCnpj(String cnpj);

    Empresa findByRazaoSocial(String razaoSocial);

    List<Empresa> findDistinctBySiglaLojaAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(String siglaLoja, List<Long> idModeloNegocio);

    List<Empresa> findDistinctByIdGrupoEconomicoInAndSiglaLojaAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(List<Long> idGrupoEconomico, String siglaLoja, List<Long> idModeloNegocio);

    Empresa findBySiglaLoja(String siglaLoja);


}

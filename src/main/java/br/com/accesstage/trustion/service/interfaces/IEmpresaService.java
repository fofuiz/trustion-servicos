package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.GrupoEconomicoDTO;
import br.com.accesstage.trustion.dto.TableauFilterDTO;
import br.com.accesstage.trustion.dto.UploadEmpresaDTO;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IEmpresaService {

    EmpresaDTO validar(EmpresaDTO empresaDTO) throws Exception;

    EmpresaDTO criar(EmpresaDTO empresaDTO) throws Exception;

    EmpresaDTO alterar(EmpresaDTO empresaDTO) throws Exception;

    EmpresaDTO pesquisar(Long idempresaDTO) throws Exception;

    boolean excluir(Long idempresaDTO) throws Exception;

    List<EmpresaDTO> listarCriterios(EmpresaDTO empresaDTO) throws Exception;

    Page<EmpresaDTO> listarCriterios(EmpresaDTO empresaDTO, Pageable pageable) throws Exception;

    List<EmpresaDTO> listarCriterios(List<GrupoEconomicoDTO> listaGrpEconFiltroDTO) throws Exception;

    List<EmpresaDTO> listarEmpresasD1(List<GrupoEconomicoDTO> listaGrpEconFiltroDTO) throws Exception;

    List<EmpresaDTO> listarEmpresasD0(List<GrupoEconomicoDTO> listaGrpEconFiltroDTO) throws Exception;

    List<TableauFilterDTO> listarEmpresasCNPJ() throws Exception;

    List<EmpresaDTO> listaEmpresasPorIdsModeloNegocio(List<Long> idsModeloNegocio) throws Exception;

    void download(HttpServletResponse response) throws Exception;

    void upload(MultipartFile multipart, UploadEmpresaDTO uploadEmpresaDTO) throws Exception;

    List<EmpresaDTO> listaEmpresasPorGrupoEcon(List<Long> listaIdGrupoEcon) throws Exception;

    List<EmpresaDTO> listaEmpresasPorUsuarioLogado() throws Exception;
    
    List<Empresa> empresasPorUsuario(Usuario usuario);
    
    List<Empresa> empresasPorUsuarioLogado();
}

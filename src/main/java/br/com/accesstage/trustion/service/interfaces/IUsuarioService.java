package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.EsqueceuSenhaDTO;
import br.com.accesstage.trustion.dto.RedefinicaoSenhaDTO;
import br.com.accesstage.trustion.dto.UsuarioDTO;
import br.com.accesstage.trustion.dto.ascartoes.UserEmpresaCADTO;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.Usuario;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUsuarioService {

    UsuarioDTO criar(UsuarioDTO usuarioTO) throws Exception;

    UsuarioDTO alterar(UsuarioDTO usuarioTO) throws Exception;

    boolean excluir(Long idUsuario) throws Exception;

    List<UsuarioDTO> listarTodos() throws Exception;

    UsuarioDTO pesquisar(Long idUsuario) throws Exception;

    void redefinirSenha(RedefinicaoSenhaDTO redefinicaoSenhaDTO) throws Exception;

    String esqueceuSenha(EsqueceuSenhaDTO esqueceuSenhaDTO) throws Exception;

    String transformaEmail(String email);

    void registrarLoginDoUsuario(Long idUsuario);

    /**
     * Método responsável por pesquisar todas as {@link Empresa}s do {@link Usuario} logado e retornar uma
     * {@link Set} <{@link UserEmpresaCADTO}> contendo informações sobre as empresas do usuário.
     * @return {@link Set} <{@link UserEmpresaCADTO}>
     */
    Set<UserEmpresaCADTO> getUserEmpresaCaList();

    Page<UsuarioDTO> listarCriterios(UsuarioDTO usuarioDTO, Pageable pageable) throws Exception;

    List<UsuarioDTO> listarCriterios(UsuarioDTO usuarioDTO) throws Exception;
}

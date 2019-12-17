package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.TransportadoraDTO;
import java.time.ZoneId;
import java.util.Date;

import br.com.accesstage.trustion.dto.UsuarioDTO;
import br.com.accesstage.trustion.enums.StatusUsuarioEnum;
import br.com.accesstage.trustion.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioConverter {

    public static Usuario paraEntidade(UsuarioDTO usuarioDTO) {

        Usuario usuario = new Usuario();

        usuario.setLogin(usuarioDTO.getLogin());
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setStatus(usuarioDTO.getStatus());
        usuario.setPrimeiroAcesso(usuarioDTO.isPrimeiroAcesso());
        usuario.setIdPerfil(usuarioDTO.getIdPerfil());
        usuario.setIdUsuarioCriacao(usuarioDTO.getIdUsuarioCriacao());
        usuario.setIdUsuarioAlteracao(usuarioDTO.getIdUsuarioAlteracao());
        usuario.setDataLogin(usuarioDTO.getDatalogin() == null ? null : usuarioDTO.getDatalogin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        usuario.setNroTelefone(usuarioDTO.getNroTelefone());
        usuario.setUsuarioTableau(usuarioDTO.getUsuarioTableau());

        return usuario;
    }

    public static UsuarioDTO paraDTO(Usuario usuario) {

        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioDTO.setLogin(usuario.getLogin());
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setStatus(usuario.getStatus());
        usuarioDTO.setPrimeiroAcesso(usuario.isPrimeiroAcesso());

        if (usuario.getStatus() != null) {
            if (usuario.getStatus().equals(StatusUsuarioEnum.ATIVO.get())) {
                usuarioDTO.setStatusDescricao("Ativo");

            } else if (usuario.getStatus().equals(StatusUsuarioEnum.INATIVO.get())) {
                usuarioDTO.setStatusDescricao("Inativo");

            } else {
                usuarioDTO.setStatusDescricao("Suspenso");
            }
        }

        usuarioDTO.setIdPerfil(usuario.getIdPerfil());
        usuarioDTO.setDataCriacao(usuario.getDataCriacao());
        usuarioDTO.setIdUsuarioCriacao(usuario.getIdUsuarioCriacao());
        usuarioDTO.setIdUsuarioAlteracao(usuario.getIdUsuarioAlteracao());
        usuarioDTO.setDataAlteracao(usuario.getDataAlteracao());
        if (usuario.getDataLogin() != null) {
            usuarioDTO.setDatalogin(Date.from(usuario.getDataLogin().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        usuarioDTO.setNroTelefone(usuario.getNroTelefone());
        usuarioDTO.setUsuarioTableau(usuario.getUsuarioTableau());

        if (usuario.getEmpresaList() != null && !usuario.getEmpresaList().isEmpty()) {
            List<EmpresaDTO> le = new ArrayList<>();
            usuario.getEmpresaList().forEach(u -> {
                EmpresaDTO e = new EmpresaDTO();
                e.setIdEmpresa(u.getIdEmpresa());
                le.add(e);
            });
            usuarioDTO.setEmpresaList(le);
        }

        if (usuario.getTransportadoraList() != null && !usuario.getTransportadoraList().isEmpty()) {
            List<TransportadoraDTO> lt = new ArrayList<>();
            usuario.getTransportadoraList().forEach(u -> {
                TransportadoraDTO t = new TransportadoraDTO();
                t.setIdTransportadora(u.getIdTransportadora());
                lt.add(t);
            });
            usuarioDTO.setTransportadoraList(lt);
        }

        return usuarioDTO;
    }

    public static Usuario paraEntidadeComID(UsuarioDTO usuarioDTO) {

        Usuario usuario = new Usuario();

        usuario.setIdUsuario(usuarioDTO.getIdUsuario());
        usuario.setLogin(usuarioDTO.getLogin());
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setStatus(usuarioDTO.getStatus());
        usuario.setPrimeiroAcesso(usuarioDTO.isPrimeiroAcesso());
        usuario.setIdPerfil(usuarioDTO.getIdPerfil());
        usuario.setIdUsuarioCriacao(usuarioDTO.getIdUsuarioCriacao());
        usuario.setIdUsuarioAlteracao(usuarioDTO.getIdUsuarioAlteracao());
        usuario.setDataLogin(usuarioDTO.getDatalogin() == null ? null : usuarioDTO.getDatalogin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        usuario.setNroTelefone(usuarioDTO.getNroTelefone());
        usuario.setUsuarioTableau(usuarioDTO.getUsuarioTableau());

        return usuario;
    }
}

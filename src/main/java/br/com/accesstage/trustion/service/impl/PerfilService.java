package br.com.accesstage.trustion.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.PerfilConverter;
import br.com.accesstage.trustion.dto.PerfilDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.enums.PerfilEnum;
import br.com.accesstage.trustion.model.Perfil;
import br.com.accesstage.trustion.repository.interfaces.IPerfilRepository;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IPerfilService;

@Service
public class PerfilService implements IPerfilService {

    @Autowired
    IPerfilRepository perfilRepository;

    @Override
    public List<PerfilDTO> listarTodosCadastro() throws Exception {

        List<PerfilDTO> listaPerfil = new ArrayList<>();
        List<Long> listaIdsPerfil = new ArrayList<>();

        if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.ADMINISTRADOR.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get());
            listaIdsPerfil.add(CodigoPerfilEnum.BPO.get());

        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_TRANSPORTADORA.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get());

        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_CLIENTE.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
            
        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());

        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_CLIENTE_CARTAO.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
            
        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_CLIENTE_NUMERARIO.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
            
        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.BPO.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE.get());            
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
            
        }

        for (Perfil perfil : perfilRepository.findByIdPerfilIn(listaIdsPerfil)) {

            listaPerfil.add(PerfilConverter.paraDTO(perfil));
        }

        return listaPerfil;
    }

    @Override
    public List<PerfilDTO> listarTodosPesquisa() throws Exception {

        List<PerfilDTO> listaPerfil = new ArrayList<>();
        List<Long> listaIdsPerfil = new ArrayList<>();

        if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.ADMINISTRADOR.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get());
            listaIdsPerfil.add(CodigoPerfilEnum.BPO.get());

        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_TRANSPORTADORA.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get());

        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_CLIENTE.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
            
        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());

        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_CLIENTE_CARTAO.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
            
        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.MASTER_CLIENTE_NUMERARIO.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
            
        } else if (UsuarioAutenticado.getPerfil().equals(PerfilEnum.BPO.get())) {
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_TRANSPORTADORA.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_VENDA_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_VENDA_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_CARTAO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.MASTER_CLIENTE_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_CARTAO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_CLIENTE_NUMERARIO.get());
            listaIdsPerfil.add(CodigoPerfilEnum.OPERADOR_TRANSPORTADORA.get());
            listaIdsPerfil.add(CodigoPerfilEnum.BPO.get());
        }

        for (Perfil perfil : perfilRepository.findByIdPerfilIn(listaIdsPerfil)) {

            listaPerfil.add(PerfilConverter.paraDTO(perfil));
        }

        return listaPerfil;
    }
}

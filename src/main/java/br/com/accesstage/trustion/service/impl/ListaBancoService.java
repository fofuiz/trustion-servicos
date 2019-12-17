package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.ListaBancoConverter;
import br.com.accesstage.trustion.dto.ListaBancoDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.model.ListaBanco;
import br.com.accesstage.trustion.repository.interfaces.IListaBancoRepository;
import br.com.accesstage.trustion.service.interfaces.IListaBancoService;
import br.com.accesstage.trustion.util.Mensagem;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static br.com.accesstage.trustion.seguranca.UsuarioAutenticado.getIdPerfil;

@Service
public class ListaBancoService implements IListaBancoService {

    @Autowired
    private IListaBancoRepository listaBancoRepository;

    @Override
    public List<ListaBancoDTO> listarTodos() throws Exception {

        List<ListaBancoDTO> dtos = new ArrayList<>();
        List<ListaBanco> entidades = listaBancoRepository.findAll();

        if (!entidades.isEmpty()) {
            for (ListaBanco entidade : entidades) {
                dtos.add(ListaBancoConverter.paraDTO(entidade));
            }
        }

        return dtos;
    }

    @Override
    public ListaBancoDTO pesquisar(Long idListaBanco) throws Exception {

        if (getIdPerfil() != CodigoPerfilEnum.ADMINISTRADOR.get()
                && getIdPerfil() != CodigoPerfilEnum.BPO.get()
                && getIdPerfil() != CodigoPerfilEnum.MASTER_TRANSPORTADORA.get()) {
            throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }

        if (!listaBancoRepository.exists(idListaBanco)) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"Banco"}));
        }

        ListaBancoDTO dto = ListaBancoConverter.paraDTO(listaBancoRepository.findOne(idListaBanco));

        return dto;
    }

    @Override
    public ListaBancoDTO pesquisarPorCodigoBanco(Integer codigoBanco) throws Exception {

        String codigoBancoTexto = StringUtils.leftPad(Integer.toString(codigoBanco), 3, '0');

        ListaBanco listaBanco = listaBancoRepository.findOneByCodigoBanco(codigoBancoTexto);

        if (listaBanco == null) {
            throw new BadRequestResponseException(Mensagem.get("msg.registro.nao.existe", new Object[]{"Banco"}));
        }

        return ListaBancoConverter.paraDTO(listaBanco);

    }
}

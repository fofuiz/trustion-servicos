package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.client.cartoes.dto.CategorizacaoTipoProdutoDTO;
import br.com.accesstage.trustion.client.cartoes.dto.filtro.FiltroDataDTO;
import br.com.accesstage.trustion.client.cartoes.resource.ITipoTransacaoResource;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.UsuarioDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.ITipoTransacaoService;
import br.com.accesstage.trustion.service.interfaces.IUsuarioService;
import br.com.accesstage.trustion.util.DataConverter;
import br.com.accesstage.trustion.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TipoTransacaoService implements ITipoTransacaoService {

    private ITipoTransacaoResource tipoTransacaoResource;

    private IEmpresaService empresaService;

    private IUsuarioService usuarioService;

    private String DATA_FORMATO = "yyyy-MM-dd";

    private Long PADRAO_INTERVALO_DIAS = 30L;

    @Autowired
    public TipoTransacaoService(ITipoTransacaoResource tipoTransacaoResource,
                                IEmpresaService empresaService,
                                IUsuarioService usuarioService) {
        this.tipoTransacaoResource = tipoTransacaoResource;
        this.empresaService = empresaService;
        this.usuarioService = usuarioService;
    }

    @Override
    public List<CategorizacaoTipoProdutoDTO> getTotalAVistaParceladoOutros() throws Exception {

        Optional<CodigoPerfilEnum> perfilOp = CodigoPerfilEnum.gerarEnum(UsuarioAutenticado.getIdPerfil());

        CodigoPerfilEnum idPerfil = null;

        List<CategorizacaoTipoProdutoDTO> tipoProdutoDTOS = null;


        if(perfilOp.isPresent())
            idPerfil = perfilOp.get();

        List<String> cnpjs;
        FiltroDataDTO filtroDataDTO;

        switch (Objects.requireNonNull(idPerfil)){
            case MASTER_CLIENTE:
                List<EmpresaDTO> empresas = empresaService.listarCriterios(new EmpresaDTO());
                cnpjs = empresas.stream().map(EmpresaDTO::getCnpj).distinct().collect(Collectors.toList());
                filtroDataDTO = new FiltroDataDTO(cnpjs,
                        DataConverter.paraString(LocalDate.now().minusDays(PADRAO_INTERVALO_DIAS), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now(), DATA_FORMATO));

                tipoProdutoDTOS = tipoTransacaoResource.getTotalAVistaParceladoOutros(filtroDataDTO);
                break;
            case OPERADOR_CLIENTE:
                UsuarioDTO usuario = usuarioService.pesquisar(UsuarioAutenticado.getIdUsuario());

                cnpjs = Arrays.asList(empresaService.pesquisar(usuario.getIdEmpresa()).getCnpj());

                filtroDataDTO = new FiltroDataDTO(cnpjs,
                        DataConverter.paraString(LocalDate.now().minusDays(PADRAO_INTERVALO_DIAS), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now(), DATA_FORMATO));
                tipoProdutoDTOS = tipoTransacaoResource.getTotalAVistaParceladoOutros(filtroDataDTO);
                break;
            default: throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));

        }

        return tipoProdutoDTOS;
    }
}

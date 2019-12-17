package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.client.cartoes.dto.CategorizacaoStatusTransacaoDTO;
import br.com.accesstage.trustion.client.cartoes.dto.OperadoraStatusDTO;
import br.com.accesstage.trustion.client.cartoes.dto.ProdutoStatusDTO;
import br.com.accesstage.trustion.client.cartoes.dto.filtro.FiltroDataDTO;
import br.com.accesstage.trustion.client.cartoes.dto.filtro.FiltroMultiplasDatasDTO;
import br.com.accesstage.trustion.client.cartoes.enums.StatusTransacaoEnum;
import br.com.accesstage.trustion.client.cartoes.resource.IStatusTransacaoResource;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.PeriodoResumoCartaoDTO;
import br.com.accesstage.trustion.dto.UsuarioDTO;
import br.com.accesstage.trustion.enums.CodigoPerfilEnum;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.seguranca.UsuarioAutenticado;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.service.interfaces.IPeriodoResumoCartaoService;
import br.com.accesstage.trustion.service.interfaces.IStatusTransacaoService;
import br.com.accesstage.trustion.service.interfaces.IUsuarioService;
import br.com.accesstage.trustion.util.DataConverter;
import br.com.accesstage.trustion.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatusTransacaoService implements IStatusTransacaoService {

    private IStatusTransacaoResource statusTransacaoResource;

    private IEmpresaService empresaService;

    private IUsuarioService usuarioService;

    private IPeriodoResumoCartaoService periodoResumoCartaoService;

    private static String DATA_FORMATO = "yyyy-MM-dd";

    @Autowired
    public StatusTransacaoService(IStatusTransacaoResource statusTransacaoResource,
                                  IEmpresaService empresaService,
                                  IUsuarioService usuarioService,
                                  IPeriodoResumoCartaoService periodoResumoCartaoService) {
        this.statusTransacaoResource = statusTransacaoResource;
        this.empresaService = empresaService;
        this.usuarioService = usuarioService;
        this.periodoResumoCartaoService = periodoResumoCartaoService;
    }

    @Override
    public List<CategorizacaoStatusTransacaoDTO> getTotalStatusTransacao() throws Exception {

        Optional<CodigoPerfilEnum> perfilOp = CodigoPerfilEnum.gerarEnum(UsuarioAutenticado.getIdPerfil());

        CodigoPerfilEnum idPerfil = null;

        List<CategorizacaoStatusTransacaoDTO> statustransaoDTOS = null;


        if(perfilOp.isPresent())
            idPerfil = perfilOp.get();

        List<String> cnpjs;
        FiltroMultiplasDatasDTO dto;

        PeriodoResumoCartaoDTO periodoResumo = periodoResumoCartaoService.pesquisar(UsuarioAutenticado.getIdUsuario());

        switch (Objects.requireNonNull(idPerfil)){
            case MASTER_CLIENTE:
                List<EmpresaDTO> empresas = empresaService.listarCriterios(new EmpresaDTO());
                cnpjs = empresas.stream().map(EmpresaDTO::getCnpj).distinct().collect(Collectors.toList());
                dto = new FiltroMultiplasDatasDTO(
                        cnpjs,
                        DataConverter.paraString(LocalDate.now().minusDays(periodoResumo.getPeriodoVenda()), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now(), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now().minusDays(periodoResumo.getPeriodoRecebimento()), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now(), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now(), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now().plusDays(periodoResumo.getPeriodoRecebimentoFuturo()), DATA_FORMATO)
                );

                statustransaoDTOS = statusTransacaoResource.getTotalStatusTransacao(dto);
                break;
            case OPERADOR_CLIENTE:
                UsuarioDTO usuario = usuarioService.pesquisar(UsuarioAutenticado.getIdUsuario());
                cnpjs = Arrays.asList(empresaService.pesquisar(usuario.getIdEmpresa()).getCnpj());
                dto = new FiltroMultiplasDatasDTO(
                        cnpjs,
                        DataConverter.paraString(LocalDate.now().minusDays(periodoResumo.getPeriodoVenda()), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now(), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now().minusDays(periodoResumo.getPeriodoRecebimento()), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now(), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now(), DATA_FORMATO),
                        DataConverter.paraString(LocalDate.now().plusDays(periodoResumo.getPeriodoRecebimentoFuturo()), DATA_FORMATO)
                );

                statustransaoDTOS = statusTransacaoResource.getTotalStatusTransacao(dto);
                break;
            default: throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }

        return statustransaoDTOS;
    }

    @Override
    public List<OperadoraStatusDTO> pesquisarTotalPorOperadoraStatus(StatusTransacaoEnum transacaoEnum) throws Exception {
        Optional<CodigoPerfilEnum> perfilOp = CodigoPerfilEnum.gerarEnum(UsuarioAutenticado.getIdPerfil());

        CodigoPerfilEnum idPerfil = null;

        List<OperadoraStatusDTO> operadoraStatusDTOS = null;


        if(perfilOp.isPresent())
            idPerfil = perfilOp.get();

        List<String> cnpjs;
        FiltroDataDTO filtroDataDTO;

        String dataInicial = "";
        String dataFinal = "";

        PeriodoResumoCartaoDTO periodoResumo = periodoResumoCartaoService.pesquisar(UsuarioAutenticado.getIdUsuario());

        switch (transacaoEnum){
            case VENDAS:
                dataInicial = DataConverter.paraString(LocalDate.now().minusDays(periodoResumo.getPeriodoVenda()), DATA_FORMATO);
                dataFinal = DataConverter.paraString(LocalDate.now(), DATA_FORMATO);
                break;
            case RECEBIMENTOS:
                dataInicial = DataConverter.paraString(LocalDate.now().minusDays(periodoResumo.getPeriodoRecebimento()), DATA_FORMATO);
                dataFinal = DataConverter.paraString(LocalDate.now(), DATA_FORMATO);
                break;
            case RECEBIMENTOS_FUTUROS:
                dataInicial = DataConverter.paraString(LocalDate.now(), DATA_FORMATO);
                dataFinal = DataConverter.paraString(LocalDate.now().plusDays(periodoResumo.getPeriodoRecebimentoFuturo()), DATA_FORMATO);
                break;
        }

        switch (Objects.requireNonNull(idPerfil)){
            case MASTER_CLIENTE:
                List<EmpresaDTO> empresas = empresaService.listarCriterios(new EmpresaDTO());
                cnpjs = empresas.stream().map(EmpresaDTO::getCnpj).distinct().collect(Collectors.toList());
                filtroDataDTO = new FiltroDataDTO(cnpjs, dataInicial, dataFinal);

                operadoraStatusDTOS = statusTransacaoResource.pesquisarTotalPorOperadoraStatus(filtroDataDTO, transacaoEnum);
                break;
            case OPERADOR_CLIENTE:
                UsuarioDTO usuario = usuarioService.pesquisar(UsuarioAutenticado.getIdUsuario());

                cnpjs = Arrays.asList(empresaService.pesquisar(usuario.getIdEmpresa()).getCnpj());

                filtroDataDTO = new FiltroDataDTO(cnpjs, dataInicial, dataFinal);
                operadoraStatusDTOS = statusTransacaoResource.pesquisarTotalPorOperadoraStatus(filtroDataDTO, transacaoEnum);
                break;
            default: throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }

        return operadoraStatusDTOS;
    }

    @Override
    public List<ProdutoStatusDTO> pesquisarTotalPorProdutoStatus(Long idOperadora, StatusTransacaoEnum transacaoEnum) throws Exception {
        Optional<CodigoPerfilEnum> perfilOp = CodigoPerfilEnum.gerarEnum(UsuarioAutenticado.getIdPerfil());

        CodigoPerfilEnum idPerfil = null;

        List<ProdutoStatusDTO> produtoStatusDTOS = null;


        if(perfilOp.isPresent())
            idPerfil = perfilOp.get();

        List<String> cnpjs;
        FiltroDataDTO filtroDataDTO;

        String dataInicial = "";
        String dataFinal = "";

        PeriodoResumoCartaoDTO periodoResumo = periodoResumoCartaoService.pesquisar(UsuarioAutenticado.getIdUsuario());

        switch (transacaoEnum){
            case VENDAS:
                dataInicial = DataConverter.paraString(LocalDate.now().minusDays(periodoResumo.getPeriodoVenda()), DATA_FORMATO);
                dataFinal = DataConverter.paraString(LocalDate.now(), DATA_FORMATO);
                break;
            case RECEBIMENTOS:
                dataInicial = DataConverter.paraString(LocalDate.now().minusDays(periodoResumo.getPeriodoRecebimento()), DATA_FORMATO);
                dataFinal = DataConverter.paraString(LocalDate.now(), DATA_FORMATO);
                break;
            case RECEBIMENTOS_FUTUROS:
                dataInicial = DataConverter.paraString(LocalDate.now(), DATA_FORMATO);
                dataFinal = DataConverter.paraString(LocalDate.now().plusDays(periodoResumo.getPeriodoRecebimentoFuturo()), DATA_FORMATO);
                break;
        }

        switch (Objects.requireNonNull(idPerfil)){
            case MASTER_CLIENTE:
                List<EmpresaDTO> empresas = empresaService.listarCriterios(new EmpresaDTO());
                cnpjs = empresas.stream().map(EmpresaDTO::getCnpj).distinct().collect(Collectors.toList());
                filtroDataDTO = new FiltroDataDTO(cnpjs, dataInicial, dataFinal);

                produtoStatusDTOS = statusTransacaoResource.pesquisarTotalPorProdutoStatus(filtroDataDTO, idOperadora, transacaoEnum);
                break;
            case OPERADOR_CLIENTE:
                UsuarioDTO usuario = usuarioService.pesquisar(UsuarioAutenticado.getIdUsuario());

                cnpjs = Arrays.asList(empresaService.pesquisar(usuario.getIdEmpresa()).getCnpj());

                filtroDataDTO = new FiltroDataDTO(cnpjs, dataInicial, dataFinal);
                produtoStatusDTOS = statusTransacaoResource.pesquisarTotalPorProdutoStatus(filtroDataDTO, idOperadora, transacaoEnum);
                break;
            default: throw new ForbiddenResponseException(Mensagem.get("msg.recurso.nao.permitido"));
        }

        return produtoStatusDTOS;
    }
}

package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.ModeloNegocioDTO;
import br.com.accesstage.trustion.model.*;
import br.com.accesstage.trustion.repository.interfaces.*;
import br.com.accesstage.trustion.seguranca.model.UsuarioDetails;
import br.com.accesstage.trustion.service.impl.EmpresaService;
import br.com.accesstage.trustion.service.impl.ModeloNegocioService;
import br.com.accesstage.trustion.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import java.util.*;
import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class ModeloNegocioServiceTest {

    @InjectMocks
    private ModeloNegocioService service;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private EmpresaService empresaService;
    
    @Mock
    private IEmpresaRepository empresaRepository;

    @Mock
    private IEmpresaModeloNegocioRepository empresaModeloNegocioRepository;

    @Mock
    private ITipoCreditoRepository tipoCreditoRepository;

    @Mock
    private ITransportadoraRepository transportadoraRepository;

    private UsuarioDetails usuarioDetails = new UsuarioDetails();
    private static final Long ID_PADRAO = 1L;
    private static final String DESCRICAO_CREDITO = "DESCRICAO CREDITO";
    private static final String RAZAO_SOCIAL = "RAZAO SOCIAL";

    @BeforeClass
    public static void setUp() {
        loadTemplates("br.com.accesstage.trustion.loader");
    }

    @Before
    public void init() throws Exception {
        usuarioDetails = TestUtil.inicializaMockAutenticacao();
        usuarioDetails.setIdPerfil(5L);
        inicializaMocksListarPorIdEmpresaTest();
    }

    @Test
    public void listarPorIdEmpresaComUsuarioQualquerValidoTest() throws Exception {
        Set<ModeloNegocioDTO> modeloNegocioDTOList = service.listarPorIdEmpresa();

        Assert.assertNotNull(modeloNegocioDTOList);
        Assert.assertEquals(1, modeloNegocioDTOList.size());
        Assert.assertEquals(DESCRICAO_CREDITO, modeloNegocioDTOList.iterator().next().getTipoCredito());
        Assert.assertEquals(RAZAO_SOCIAL, modeloNegocioDTOList.iterator().next().getTransportadora());
    }


    @Test
    public void listarPorIdEmpresaComIdTipoCreditoNuloTest() throws Exception {
        usuarioDetails.setIdPerfil(5l);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(usuarioDetails);

        Set<ModeloNegocioDTO> modeloNegocioDTOList = service.listarPorIdEmpresa();

        Assert.assertNotNull(modeloNegocioDTOList);
        Assert.assertEquals(1, modeloNegocioDTOList.size());
        Assert.assertEquals(DESCRICAO_CREDITO, modeloNegocioDTOList.iterator().next().getTipoCredito());
        Assert.assertEquals(RAZAO_SOCIAL, modeloNegocioDTOList.iterator().next().getTransportadora());
    }

    /**
     * Método responsável por criar os objetos e os mocks utilizados para rodar os vários cenários de teste
     * do método listarPorIdEmpresa
     * @throws Exception
     */
    private void inicializaMocksListarPorIdEmpresaTest() throws Exception {
        EmpresaDTO empresaUsuarioLogadoDTO1 = new EmpresaDTO();
        empresaUsuarioLogadoDTO1.setIdEmpresa(ID_PADRAO);

        List<EmpresaDTO> empresaUsuarioLogadoList = new ArrayList<>();
        empresaUsuarioLogadoList.add(empresaUsuarioLogadoDTO1);

        Empresa empresaUsuarioLogado = new Empresa();
        empresaUsuarioLogado.setIdEmpresa(ID_PADRAO);

        ModeloNegocio modeloNegocio = new ModeloNegocio();
        modeloNegocio.setIdTipoCredito(ID_PADRAO);

        EmpresaModeloNegocio empresaModeloNegocio = new EmpresaModeloNegocio();
        empresaModeloNegocio.setModeloNegocio(modeloNegocio);

        TipoCredito tipoCredito = new TipoCredito();
        tipoCredito.setDescricao(DESCRICAO_CREDITO);

        Transportadora transportadora = new Transportadora();
        transportadora.setRazaoSocial(RAZAO_SOCIAL);

        Usuario usuario = new Usuario();
        usuario.setTransportadoraList(Arrays.asList(transportadora));

        Mockito.when(empresaService.listaEmpresasPorUsuarioLogado()).thenReturn(empresaUsuarioLogadoList);
        Mockito.when(empresaRepository.findOne(Mockito.anyLong())).thenReturn(empresaUsuarioLogado);
        Mockito.when(empresaModeloNegocioRepository.findByEmpresa(Mockito.any(Empresa.class))).thenReturn(Arrays.asList(empresaModeloNegocio));
        Mockito.when(tipoCreditoRepository.findOne(Mockito.anyLong())).thenReturn(tipoCredito);
        Mockito.when(transportadoraRepository.findOne(Mockito.anyLong())).thenReturn(transportadora);
        Mockito.when(usuarioRepository.findOne(Mockito.anyLong())).thenReturn(usuario);

    }
}

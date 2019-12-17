package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.ascartoes.model.EmpresaCA;
import br.com.accesstage.trustion.dto.ascartoes.UserEmpresaCADTO;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.service.impl.EmpresaService;
import br.com.accesstage.trustion.service.impl.UsuarioService;
import br.com.accesstage.trustion.service.impl.ascartoes.EmpresaCaService;
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
import org.springframework.test.context.ContextConfiguration;
import java.util.Collections;
import java.util.Set;

import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private EmpresaCaService empresaCaService;

    private static final String RAZAO_SOCIAL = "Razao Social Empresa 1";
    private static final String CNPJ = "000000000";
    private static final Long ID_PADRAO = 1L;


    @BeforeClass
    public static void setUp() {
        loadTemplates("br.com.accesstage.trustion.loader");
    }

    @Before
    public void init(){
        TestUtil.inicializaMockAutenticacao();
        carregaMocksGetUserEmpresaCaListTest();
    }

    @Test
    public void getUserEmpresaCaListTest(){
        Set<UserEmpresaCADTO> userEmpresaCaList = usuarioService.getUserEmpresaCaList();
        UserEmpresaCADTO empresaCADTO = userEmpresaCaList.iterator().next();

        Assert.assertNotNull(userEmpresaCaList);
        Assert.assertEquals(1, userEmpresaCaList.size());
        Assert.assertEquals(CNPJ, empresaCADTO.getCnpj());
        Assert.assertEquals(RAZAO_SOCIAL, empresaCADTO.getRazaoSocial());
        Assert.assertEquals(ID_PADRAO, empresaCADTO.getIdEmpresaCa());
    }

    /**
     * Método responsável por inicializar todos os objetos, variáveis e mocks necessários para o teste do método
     * getuserEmpresaCaList()
     */
    private void carregaMocksGetUserEmpresaCaListTest() {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(RAZAO_SOCIAL);
        empresa.setCnpj(CNPJ);

        EmpresaCA empresaCA = new EmpresaCA();
        empresaCA.setId(ID_PADRAO);
        empresaCA.setTpoRemessa(ID_PADRAO.intValue());
        empresaCA.setCodigoSegmento(ID_PADRAO.intValue());

        Mockito.when(usuarioRepository.findOne(Mockito.anyLong())).thenReturn(new Usuario());
        Mockito.when(empresaService.empresasPorUsuario(Mockito.any(Usuario.class))).thenReturn(Collections.singletonList(empresa));
        Mockito.when(empresaCaService.buscaEmpresa(Mockito.anyString())).thenReturn(empresaCA);
    }
}



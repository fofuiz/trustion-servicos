package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.ascartoes.model.TaxaAdministrativa;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAdministrativaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxasAdministrativasCadastroDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaCadastroDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.TaxaAdministrativaRepository;
import br.com.accesstage.trustion.repository.ascartoes.interfaces.ITaxaAdministrativaRepository;
import br.com.accesstage.trustion.service.ascartoes.impl.TaxaAdministrativaService;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IOperadoraCAService;
import br.com.accesstage.trustion.service.ascartoes.interfaces.ILojaCAService;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IPontoVendaCAService;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IProdutoOperadoraCAService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import static br.com.six2six.fixturefactory.Fixture.from;
import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class TaxaAdministrativaServiceTest {

    private static final Pageable PAGEABLE = new PageRequest(0, 1);

    @InjectMocks
    private final TaxaAdministrativaService service = new TaxaAdministrativaService();

    @Mock
    private TaxaAdministrativaRepository repository;

    @Mock
    private ITaxaAdministrativaRepository iRepository;

    @Mock
    private ILojaCAService lojaCAService;

    @Mock
    private IPontoVendaCAService pontoVendaCAService;

    @Mock
    private IOperadoraCAService operadoraCAService;

    @Mock
    private IProdutoOperadoraCAService produtoOperadoraCAService;
    
    private FiltroTaxaAdministrativaDTO filtro;
    private TaxaAdministrativaDTO taxa;
    private TaxaAdministrativa taxaEntity;
    private TaxaAdministrativaCadastroDTO taxaCadastro;
    private List<TaxaAdministrativaDTO> response;
    private List<TaxaAdministrativaDTO> validPage;
    private FiltroTaxasAdministrativasCadastroDTO filtroCadastro = new FiltroTaxasAdministrativasCadastroDTO();
    private List<TaxaAdministrativaCadastroDTO> responseCadastro;
    private List<TaxaAdministrativa> listTaxaAdministrativa;
    private List<TaxaAdministrativa> taxas;

    @BeforeClass
    public static void setUp() {
        loadTemplates("br.com.accesstage.trustion.loader");
    }

    @Before
    public void init() {
        filtro = from(FiltroTaxaAdministrativaDTO.class).gimme("valid");
        taxa = from(TaxaAdministrativaDTO.class).gimme("valid");
        response = new ArrayList<>();
        response.add(taxa);
        filtroCadastro = from(FiltroTaxasAdministrativasCadastroDTO.class).gimme("valid");
        taxaCadastro = from(TaxaAdministrativaCadastroDTO.class).gimme("valid");
        responseCadastro = new ArrayList<>();
        responseCadastro.add(taxaCadastro);
        taxaEntity = from(TaxaAdministrativa.class).gimme("valid");
        taxas = new ArrayList<>();
        taxas.add(taxaEntity);
        listTaxaAdministrativa = new ArrayList<>();
        listTaxaAdministrativa.add(taxaEntity);
    }

    @Test
    public void testPesquisar() {
        when(repository.consulta(filtro)).thenReturn(response);
        response = service.pesquisar(filtro);
        assertNotNull(response);
    }

    @Test
    public void testPesquisarPage() {
        when(repository.consultaPage(filtro, PAGEABLE)).thenReturn(validPage);
        response = service.pesquisar(filtro);
        assertNotNull(response);
    }

//    @Test
//    public void testPesquisarCadastro() {
//        when(repository.consultarCadastro(filtroCadastro)).thenReturn(listTaxaAdministrativa);
//        responseCadastro = service.pesquisarCadastro(filtroCadastro);
//        assertNotNull(responseCadastro);
//    }

    @Test
    public void testSalvar() {
        when(iRepository.save(taxas)).thenReturn(taxas);
        taxas = service.salvar(responseCadastro);
        assertNotNull(taxas);
    }

}

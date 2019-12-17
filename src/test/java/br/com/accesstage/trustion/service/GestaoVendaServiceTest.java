package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.dto.ascartoes.FiltroGestaoVendaDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.dto.ascartoes.SemaforoDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.GestaoVendaRepository;
import br.com.accesstage.trustion.service.impl.ascartoes.GestaoVendaService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import static br.com.six2six.fixturefactory.Fixture.from;
import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class GestaoVendaServiceTest {

    @InjectMocks
    private final GestaoVendaService service = new GestaoVendaService();

    @Mock
    private GestaoVendaRepository repository;

    private FiltroGestaoVendaDTO filtro;
    private GestaoVendasDTO gestaoVendasDTO;
    private SemaforoDTO semaforoDTO;
    private List<GestaoVendasDTO> response;
    private List<SemaforoDTO> semaforoResponse;

    @BeforeClass
    public static void setUp() {
        loadTemplates("br.com.accesstage.trustion.loader");
    }

    @Before
    public void init() {
        filtro = from(FiltroGestaoVendaDTO.class).gimme("valid");
        gestaoVendasDTO = from(GestaoVendasDTO.class).gimme("valid");
        semaforoDTO = from(SemaforoDTO.class).gimme("valid");
        response = new ArrayList<>();
        response.add(gestaoVendasDTO);
        semaforoResponse = new ArrayList<>();
        semaforoResponse.add(semaforoDTO);
    }

    @Test
    public void testPesquisarVisaoLojaEmpresaCiaAerea() throws Exception {
        when(repository.buscarDivergenciaLojaCiasAereas(filtro)).thenReturn(response);
        when(repository.buscarDivergenciaLojaSemaforoCIAS(filtro)).thenReturn(response);
        response = service.pesquisar(filtro);
        assertNotNull(response);
    }

    @Test
    public void testPesquisarVisaoLoja() throws Exception {
        when(repository.buscarDivergenciaLoja(filtro)).thenReturn(response);
        when(repository.buscarDivergenciaLojaSemaforo(filtro)).thenReturn(response);
        response = service.pesquisar(filtro);
        assertNotNull(response);
    }

    @Test
    public void testPesquisar() throws Exception {
        when(repository.consultaBandeiraPaginada(filtro)).thenReturn(response);
        when(repository.atualizaSemaforoConsultaOperadora(filtro)).thenReturn(response);
        response = service.pesquisar(filtro);
        assertNotNull(response);
    }

    @Test
    public void testConsultaBandeira() {
        when(repository.consultaBandeira(filtro)).thenReturn(response);
        response = service.consultaBandeira(filtro);
        assertNotNull(response);
    }

    @Test
    public void testBuscarDivergenciaLoja() {
        when(repository.buscarDivergenciaLoja(filtro)).thenReturn(response);
        response = service.buscarDivergenciaLoja(filtro);
        assertNotNull(response);
    }

    @Test
    public void testCarregarSemaforoZerado() {
        when(repository.carregarSemaforoZerado()).thenReturn(semaforoResponse);
        semaforoResponse = service.carregarSemaforoZerado();
        assertNotNull(semaforoResponse);
    }

}

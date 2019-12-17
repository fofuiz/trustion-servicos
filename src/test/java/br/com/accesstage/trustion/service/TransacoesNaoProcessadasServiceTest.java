package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTransacoesNaoProcessadasDTO;
import br.com.accesstage.trustion.dto.ascartoes.TransacoesNaoProcessadasDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.TransacoesNaoProcessadasCARepository;
import br.com.accesstage.trustion.service.impl.ascartoes.TransacoesNaoProcessadasService;
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

/**
 *
 * @author raphael
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class TransacoesNaoProcessadasServiceTest {

    @InjectMocks
    private final TransacoesNaoProcessadasService service = new TransacoesNaoProcessadasService();

    @Mock
    private TransacoesNaoProcessadasCARepository repository;

    private FiltroTransacoesNaoProcessadasDTO filtro;
    private TransacoesNaoProcessadasDTO transacoesNaoProcessadasDTO;
    private List<TransacoesNaoProcessadasDTO> response;

    @BeforeClass
    public static void setUp() {
        loadTemplates("br.com.accesstage.trustion.loader");
    }

    @Before
    public void init() {
        filtro = from(FiltroTransacoesNaoProcessadasDTO.class).gimme("valid");
        transacoesNaoProcessadasDTO = from(TransacoesNaoProcessadasDTO.class).gimme("valid");
        response = new ArrayList<>();
        response.add(transacoesNaoProcessadasDTO);
    }

    @Test
    public void testBuscarHistoricoTransacoesNaoProcessadas() {
        when(repository.buscarHistoricoTransacoesNaoProcessadas(filtro)).thenReturn(response);
        response = service.buscarHistoricoTransacoesNaoProcessadas(filtro);
        assertNotNull(response);
    }

    @Test
    public void testBuscarHistoricoTransacoesNaoProcessadasAntecipacao() {
        when(repository.buscarHistoricoTransacoesNaoProcessadasAntecipacao(filtro)).thenReturn(response);
        response = service.buscarHistoricoTransacoesNaoProcessadasAntecipacao(filtro);
        assertNotNull(response);
    }
}

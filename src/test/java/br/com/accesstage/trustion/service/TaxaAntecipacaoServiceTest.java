package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAntecipacaoDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAntecipacaoDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.TaxaAntecipacaoRepository;
import br.com.accesstage.trustion.service.ascartoes.impl.TaxaAntecipacaoService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import static br.com.six2six.fixturefactory.Fixture.from;
import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class TaxaAntecipacaoServiceTest {

    private static final Pageable PAGEABLE = new PageRequest(0, 1);

    @InjectMocks
    private final TaxaAntecipacaoService service = new TaxaAntecipacaoService();

    @Mock
    private TaxaAntecipacaoRepository repository;

    private FiltroTaxaAntecipacaoDTO filtro;
    private TaxaAntecipacaoDTO taxa = new TaxaAntecipacaoDTO();
    private List<TaxaAntecipacaoDTO> response;
    private Page<TaxaAntecipacaoDTO> validPage;

    @BeforeClass
    public static void setUp() {
        loadTemplates("br.com.accesstage.trustion.loader");
    }

    @Before
    public void init() {
        filtro = from(FiltroTaxaAntecipacaoDTO.class).gimme("valid");
        taxa = from(TaxaAntecipacaoDTO.class).gimme("valid");
        response = new ArrayList<>();
        response.add(taxa);
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

}

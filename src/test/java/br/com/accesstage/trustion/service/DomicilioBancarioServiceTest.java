package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.asconciliacao.model.DomicilioBancario;
import br.com.accesstage.trustion.dto.FiltroDomicilioBancarioDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoDomicilioBancarioDTO;
import br.com.accesstage.trustion.repository.impl.DomicilioBancarioRepository;
import br.com.accesstage.trustion.service.impl.DomicilioBancarioService;
import br.com.accesstage.trustion.util.TestUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;

import static br.com.six2six.fixturefactory.Fixture.from;
import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class DomicilioBancarioServiceTest {

    @InjectMocks
    private final DomicilioBancarioService service = new DomicilioBancarioService();

    @Mock
    private Logger logger;

    @Mock
    private DomicilioBancarioRepository repository;

    private FiltroDomicilioBancarioDTO filtro;
    private DomicilioBancario db;
    private List<DomicilioBancario> ldb;
    private ResultadoDomicilioBancarioDTO rdb;
    private List<ResultadoDomicilioBancarioDTO> lrdb;

    @BeforeClass
    public static void setUp() {
        loadTemplates("br.com.accesstage.trustion.loader");
    }

    @Before
    public void init() throws Exception {
        when(logger.isInfoEnabled()).thenReturn(false);
        TestUtil.setFinalStatic(service, DomicilioBancarioService.class.getDeclaredField("LOGGER"), logger);

        filtro = from(FiltroDomicilioBancarioDTO.class).gimme("valid");
        db = from(DomicilioBancario.class).gimme("valid");
        ldb = new ArrayList<>();
        ldb.add(db);
        rdb = from(ResultadoDomicilioBancarioDTO.class).gimme("valid");
        lrdb = new ArrayList<>();
        lrdb.add(rdb);
    }

    @Test
    public void testPesquisarDetalheDomicilioBancario() throws Exception {
        when(repository.perquisarDetalheDomicilioBancario(filtro)).thenReturn(ldb);
        lrdb = service.perquisarDetalheDomicilioBancario(filtro);
        assertNotNull(lrdb);
    }

    @Test
    public void testPesquisarTodosBancos() throws Exception {
        when(repository.pesquisarTodosBancos(filtro)).thenReturn(ldb);
        lrdb = service.pesquisarTodosBancos(filtro);
        assertNotNull(lrdb);
    }

}

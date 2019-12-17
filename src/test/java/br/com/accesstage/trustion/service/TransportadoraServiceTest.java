package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.dto.TransportadoraDTO;
import br.com.accesstage.trustion.model.Transportadora;
import br.com.accesstage.trustion.repository.interfaces.ITransportadoraRepository;
import br.com.accesstage.trustion.seguranca.model.UsuarioDetails;
import br.com.accesstage.trustion.service.impl.TransportadoraService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static br.com.six2six.fixturefactory.Fixture.from;
import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class TransportadoraServiceTest {

    @InjectMocks
    private final TransportadoraService service = new TransportadoraService();

    @Mock
    private ITransportadoraRepository repository;

    private TransportadoraDTO filtro;
    private TransportadoraDTO transportadoraDTO = new TransportadoraDTO();
    private Transportadora transportadora = new Transportadora();
    private List<Transportadora> response;
    private List<TransportadoraDTO> responseDTO;

    @BeforeClass
    public static void setUp() {
        loadTemplates("br.com.accesstage.trustion.loader");
    }

    @Before
    public void init() {
        filtro = from(TransportadoraDTO.class).gimme("valid");
        transportadoraDTO = from(TransportadoraDTO.class).gimme("valid");
        transportadora = from(Transportadora.class).gimme("valid");
        response = new ArrayList<>();
        responseDTO = new ArrayList<>();
        response.add(transportadora);
    }

    @Test
    public void testListarCriterios() throws Exception {
        when(repository.findAll()).thenReturn(response);

        UsuarioDetails usuarioDetails = new UsuarioDetails();
        usuarioDetails.setIdPerfil(1l);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(usuarioDetails);

        responseDTO = service.listarCriterios(filtro);
        assertNotNull(responseDTO);
    }


}

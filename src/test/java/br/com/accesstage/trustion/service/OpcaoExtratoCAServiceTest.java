package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.ascartoes.model.OpcaoExtratoCA;
import br.com.accesstage.trustion.repository.ascartoes.interfaces.IOpcaoExtratoCARepository;
import br.com.accesstage.trustion.service.ascartoes.impl.OpcaoExtratoCAService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class OpcaoExtratoCAServiceTest {

    @Mock
    private IOpcaoExtratoCARepository repository;

    @InjectMocks
    private final OpcaoExtratoCAService service = new OpcaoExtratoCAService();

    @Test
    public void testCarregarComboOperadora() {
        when(repository.findAllByOrderByCodigoOpcaoExtratoAsc()).thenReturn(new ArrayList<>());
        List<OpcaoExtratoCA> response = service.carregarComboOpcaoExtrato();
        assertNotNull(response);
    }
}

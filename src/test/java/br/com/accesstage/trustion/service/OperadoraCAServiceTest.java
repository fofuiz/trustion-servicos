package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.ascartoes.model.OperadoraCA;
import br.com.accesstage.trustion.repository.ascartoes.interfaces.IOperadoraCARepository;
import br.com.accesstage.trustion.service.ascartoes.impl.OperadoraCAService;
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
public class OperadoraCAServiceTest {

    @Mock
    private IOperadoraCARepository repository;

    @InjectMocks
    private final OperadoraCAService service = new OperadoraCAService();

    @Test
    public void testCarregarComboOperadora() {
        when(repository.findByFlgFiltroPortalOrderByNmeExibicaoPortalAsc(true)).thenReturn(new ArrayList<>());
        List<OperadoraCA> response = service.carregarComboOperadora(true);
        assertNotNull(response);
    }
}
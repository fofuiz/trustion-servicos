package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.ascartoes.model.LojaCA;
import br.com.accesstage.trustion.ascartoes.model.PontoVendaCA;
import br.com.accesstage.trustion.dto.ascartoes.LojaOuPontoVendaDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.PontoVendaCARepository;
import br.com.accesstage.trustion.repository.ascartoes.interfaces.ILojaCARepository;
import br.com.accesstage.trustion.service.ascartoes.impl.LojaCAService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class LojaCAServiceTest {

    @InjectMocks
    private LojaCAService lojaCAService;

    @Mock
    private ILojaCARepository lojaCARepository;

    @Mock
    private PontoVendaCARepository pontoVendaCARepository;

    private static final Long ID_PADRAO = Long.valueOf(1);

    @BeforeClass
    public static void setUp() {
        loadTemplates("br.com.accesstage.trustion.loader");
    }

    @Before
    public void init() {
        LojaCA lojaCA = new LojaCA();
        lojaCA.setId(ID_PADRAO);
        lojaCA.setNome("Loja CA 1");

        List<LojaCA> lojaCAList = new ArrayList<>();
        lojaCAList.add(lojaCA);

        PontoVendaCA pontoVendaCA = new PontoVendaCA();
        pontoVendaCA.setIdPontoVenda(ID_PADRAO);
        pontoVendaCA.setNumeroTerminal(String.valueOf(ID_PADRAO));

        Mockito.when(lojaCARepository.findByIdEmpresaOrderByNomeAsc(Mockito.anyLong())).thenReturn(lojaCAList);
        Mockito.when(pontoVendaCARepository.listarTodosNulos(Mockito.anyLong())).thenReturn(Arrays.asList(pontoVendaCA));
    }

    @Test
    public void carregarComboLojaIdEmpresaValidoTest(){
        List<LojaOuPontoVendaDTO> lojaOuPontoVendaDTOList = lojaCAService.carregarComboLoja(ID_PADRAO);

        Assert.assertNotNull(lojaOuPontoVendaDTOList);
        Assert.assertEquals(2, lojaOuPontoVendaDTOList.size());
    }
}

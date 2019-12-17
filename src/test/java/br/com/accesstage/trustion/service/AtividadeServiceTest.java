/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.accesstage.trustion.service;

import br.com.accesstage.trustion.Configuracao;
import br.com.accesstage.trustion.dto.AtividadeDTO;
import br.com.accesstage.trustion.model.Atividade;
import br.com.accesstage.trustion.model.Ocorrencia;
import br.com.accesstage.trustion.model.Usuario;
import br.com.accesstage.trustion.repository.interfaces.IAtividadeRepository;
import br.com.accesstage.trustion.repository.interfaces.IOcorrenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.IUsuarioRepository;
import br.com.accesstage.trustion.service.impl.AtividadeService;
import static br.com.six2six.fixturefactory.Fixture.from;
import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author elaine.querido
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Configuracao.class)
public class AtividadeServiceTest {
    
    @InjectMocks
    private final AtividadeService service = new AtividadeService();
    
    
    @Mock
    private Logger logger;
    
    @Mock
    private IAtividadeRepository mockAtividadeRepository;
    
    @Mock
    private IUsuarioRepository mockUsuarioRepository;
    
    @Mock
    private IOcorrenciaRepository ocorrenciaRepository;

    
    private Atividade atividade;
    private List<Atividade> listAtividade;
    private List<AtividadeDTO> listAtividadeDTO;
    private Usuario usuario;
    private Ocorrencia ocorrencia;

    @BeforeClass
    public static void setUp() {
        loadTemplates("br.com.accesstage.trustion.loader");
    }
    
    @Before
    public void init() throws Exception {
        
        atividade = from(Atividade.class).gimme("valid_d1");
        listAtividade = new ArrayList<>();
        listAtividade.add(atividade);
        usuario = from(Usuario.class).gimme("valid");
        ocorrencia = from(Ocorrencia.class).gimme("valid_d1");
    }

    
    
    @Test
    public void testListarTodos() throws Exception {
        when(mockAtividadeRepository.findAll()).thenReturn(listAtividade);
        when(ocorrenciaRepository.findOne(atividade.getIdOcorrencia())).thenReturn(ocorrencia);
        when(mockUsuarioRepository.findOne(usuario.getIdUsuario())).thenReturn(usuario);
        listAtividadeDTO = service.listarTodos();
        assertNotNull(listAtividadeDTO);
    }
    

    
}

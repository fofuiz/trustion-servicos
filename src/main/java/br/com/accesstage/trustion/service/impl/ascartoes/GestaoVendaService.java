package br.com.accesstage.trustion.service.impl.ascartoes;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.FiltroGestaoVendaDTO;
import br.com.accesstage.trustion.dto.ascartoes.GestaoVendasDTO;
import br.com.accesstage.trustion.dto.ascartoes.SemaforoDTO;
import br.com.accesstage.trustion.repository.ascartoes.impl.GestaoVendaRepository;
import br.com.accesstage.trustion.service.interfaces.ascartoes.IGestaoVendasService;
import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestaoVendaService implements IGestaoVendasService {

    private static final int VISAO_LOJA = 1;
    private static final int EMPRESA_CIA_AEREA = 7;

    @Log
    private static Logger LOGGER;

    @Autowired
    private GestaoVendaRepository repository;

    @Override
    public List<GestaoVendasDTO> pesquisar(FiltroGestaoVendaDTO filtro) throws Exception {
        List<GestaoVendasDTO> vos = null;

        try {
            if (filtro.getIdVisao().equals(VISAO_LOJA)) {
                if (filtro.getEmpresa().getCodigoSegmento() == EMPRESA_CIA_AEREA) {
                    vos = repository.buscarDivergenciaLojaCiasAereas(filtro);
                } else {
                    vos = repository.buscarDivergenciaLoja(filtro);
                }
            } else {
                vos = repository.consultaBandeiraPaginada(filtro);
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao realizar o LOAD GESTAO VENDAS - " + e.getMessage());
        }

        return vos;
    }

    @Override
    public List<GestaoVendasDTO> pesquisarSemaforo(FiltroGestaoVendaDTO filtro) throws Exception {
        List<GestaoVendasDTO> vos = null;

        try {
            if (filtro.getIdVisao().equals(VISAO_LOJA)) {
                if (filtro.getEmpresa().getCodigoSegmento() == EMPRESA_CIA_AEREA) {
                    vos = repository.buscarDivergenciaLojaSemaforoCIAS(filtro);
                } else {
                    vos = repository.buscarDivergenciaLojaSemaforo(filtro);
                }
            } else {
                vos = repository.atualizaSemaforoConsultaOperadora(filtro);
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao realizar o LOAD GESTAO VENDAS - " + e.getMessage());
        }

        return vos;
    }

    @Override
    public List<GestaoVendasDTO> consultaBandeira(FiltroGestaoVendaDTO filtro) {
        return repository.consultaBandeira(filtro);
    }

    @Override
    public List<GestaoVendasDTO> buscarDivergenciaLoja(FiltroGestaoVendaDTO filtro) {
        return repository.buscarDivergenciaLoja(filtro);
    }

    @Override
    public List<SemaforoDTO> carregarSemaforoZerado() {
        return repository.carregarSemaforoZerado();
    }
}

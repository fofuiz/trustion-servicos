package br.com.accesstage.trustion.repository.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.AluguelEquipamentosDTO;
import br.com.accesstage.trustion.dto.ascartoes.BandeiraEquipamentosDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroAluguelEquipamentosDTO;
import br.com.accesstage.trustion.enums.LogsEnum;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.accesstage.trustion.repository.constantes.ConstantesAluguelEquipamento;
import static br.com.accesstage.trustion.util.ascartoes.Utils.formataDataRelatorio;

@Component
public class AluguelEquipamentosRepositoty {

    @PersistenceContext(unitName = "cartoesEntityManagerFactory")
    private EntityManager em;

    @Log
    private static Logger LOGGER;

    public List<BandeiraEquipamentosDTO> buscarAluguelEquipamento(FiltroAluguelEquipamentosDTO filtroAluguelEquipamentosDTO,
                                                                 String empIds){
        LOGGER.info(LogsEnum.ALUQEUIP007.texto(),
                filtroAluguelEquipamentosDTO.getDataDe(),
                filtroAluguelEquipamentosDTO.getDataAte(),
                filtroAluguelEquipamentosDTO.getEmpId());
        List<BandeiraEquipamentosDTO> aluguelEquipamentosDTOS;


        try {
            Query query = em.createNativeQuery(ConstantesAluguelEquipamento.BUSCAR_ALUGUEL)
                    .setParameter("idsEmp", empIds)
                    .setParameter("dtaInicio", formataDataRelatorio(filtroAluguelEquipamentosDTO.getDataDe()))
                    .setParameter("dtaFim", formataDataRelatorio(filtroAluguelEquipamentosDTO.getDataAte()));

            aluguelEquipamentosDTOS = criarLista(query.getResultList());
        } catch (Exception e) {
            LOGGER.info(LogsEnum.ALUQEUIP008.texto(),
                    filtroAluguelEquipamentosDTO.getDataDe(),
                    filtroAluguelEquipamentosDTO.getDataAte(),
                    filtroAluguelEquipamentosDTO.getEmpId());
            throw e;
        }
        return aluguelEquipamentosDTOS;
    }

    private List<BandeiraEquipamentosDTO> criarLista(List<Object[]> dados){
        if(dados != null){
            List<AluguelEquipamentosDTO> collectEquipamentos = dados.stream()
                    .map(objects -> builderAluguelEquipamentosDTO(objects))
                    .collect(Collectors.toList());

            final Map<String, BigDecimal> collectFlags = collectEquipamentos.stream()
                    .collect(Collectors.groupingBy(
                            AluguelEquipamentosDTO::getBandeira,
                            Collectors.reducing(BigDecimal.ZERO, AluguelEquipamentosDTO::getValor, BigDecimal::add))
                            );

            return collectEquipamentos
                    .stream()
                    .collect(Collectors.groupingBy(AluguelEquipamentosDTO::getBandeira))
                    .entrySet()
                    .stream()
                    .map(stringListEntry -> buildBanderiraEquipamentosDTO(stringListEntry.getValue(),collectFlags))
                    .collect(Collectors.toList());
        }
        return null;
    }
    private AluguelEquipamentosDTO builderAluguelEquipamentosDTO(Object[] objects){
        return AluguelEquipamentosDTO.builder()
                .dataCredito((BigDecimal) objects[0])
                .bandeira((String) objects[1])
                .valor((BigDecimal) objects[2])
                .loja((String) objects[3])
                .build();
    }

    private BandeiraEquipamentosDTO buildBanderiraEquipamentosDTO(List<AluguelEquipamentosDTO> aluguelEquipamentosDTOList, Map<String,BigDecimal> entry){
        return BandeiraEquipamentosDTO.builder()
                .aluguelEquipamentos(aluguelEquipamentosDTOList)
                .totalBandeira(entry.get(aluguelEquipamentosDTOList.get(0).getBandeira()))
                .build();
    }
}

package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.RelatorioAnaliticoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.RelatorioAnaliticoCreditoD1;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface IRelatorioAnaliticoCreditoD1Repository extends JpaRepository<RelatorioAnaliticoCreditoD1, Long>, JpaSpecificationExecutor<RelatorioAnaliticoCreditoD1> {

    RelatorioAnaliticoCreditoD1 findOneByIdOcorrencia(Long idOcorrencia);

    RelatorioAnaliticoCreditoD1 findOneByIdConciliacao(Long idConciliacao);

    boolean existsByGtv(String gtv);

    boolean existsByIdOcorrencia(Long idOcorrencia);

    Stream<RelatorioAnaliticoCreditoD1> findAllByIdEmpresaInAndDataConferenciaBetween(List<Long> idsEmpresas, Date dataIncial, Date dataFinal);
    
    Optional<RelatorioAnaliticoCreditoD1> findOneByIdOcorrenciaAndIdEmpresaAndIdGrupoEconomicoAndStatusConciliacaoNotAndIdTransportadora(Long idOcorrencia, Long idEmpresa, Long idGrupo, String status, Long idTransportadora);
}

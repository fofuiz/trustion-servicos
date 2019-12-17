package br.com.accesstage.trustion.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.model.RelatorioAnaliticoCredito;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface IRelatorioAnaliticoCreditoRepository extends JpaRepository<RelatorioAnaliticoCredito, Long>, JpaSpecificationExecutor<RelatorioAnaliticoCredito> {

    RelatorioAnaliticoCredito findOneByIdOcorrencia(Long idOcorrencia);

    RelatorioAnaliticoCredito findOneByIdConciliacao(Long idConciliacao);

    boolean existsByIdOcorrencia(Long idOcorrencia);

    Stream<RelatorioAnaliticoCredito> findAllByIdEmpresaInAndAndDataCorteBetween(List<Long> idsEmpresas, Date dataInicial, Date dataFinal);
    
    Optional<RelatorioAnaliticoCredito> findOneByIdOcorrenciaAndIdEmpresaAndIdGrupoEconomicoAndStatusConciliacaoNotAndIdTransportadora(Long idOcorrencia, Long idEmpresa, Long idGrupo, String status, Long idTransportadora);
}

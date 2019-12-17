package br.com.accesstage.trustion.repository.interfaces;

import br.com.accesstage.trustion.model.Ocorrencia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IOcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {

    List<Ocorrencia> findAllByIdOcorrenciaMescla(Long idOcorrencia);

    List<Ocorrencia> findAllByIsOcorrenciaD1(boolean isOcorrenciaD1);
    
    // id_ocorrencia <> ?
    // id_tipo_status_ocorrencia <> ?
    // id_status_mescla is null or id_status_mescla <>
    // is_ocorrencia_d1 = true
    // id_ocorrencia_mescla is null or id_ocorrencia_mescla = ?
    
    //List<Ocorrencia> findAllByIdOcorrenciaNotAndIdTipoStatusOcorrenciaNotAndIdStatusMesclaIsNullOrIdStatusMesclaNotAndIsOcorrenciaD1AndIdOcorrenciaMesclaIsNullOrIdOcorrenciaMescla(Long idOcorrencia, Long idTipoStatusOcorrencia, Long idStatusMescla, boolean isOcorrenciaD1, Long idOcorrencia2);
    @Query("select o from t_ocorrencia o "
            + "where o.idOcorrencia <> ?1 "
            + "and o.idTipoStatusOcorrencia <> ?2 "
            + "and (o.idStatusMescla is null or o.idStatusMescla <> ?3) "
            + "and o.isOcorrenciaD1 = ?4 "
            + "and (o.idOcorrenciaMescla is null or o.idOcorrenciaMescla = ?5)"
            + "and o.idTransportadora = ?6")
    List<Ocorrencia> findOcorrencias2Mescla(Long idOcorrencia, Long idTipoStatusOcorrencia, Long idStatusMescla, boolean isOcorrenciaD1, Long idOcorrencia2, Long idTransportadora);

}

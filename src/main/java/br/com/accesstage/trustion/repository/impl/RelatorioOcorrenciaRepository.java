package br.com.accesstage.trustion.repository.impl;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.RelatorioOcorrenciaDTO;
import br.com.accesstage.trustion.model.Atividade;
import br.com.accesstage.trustion.model.SlaAtendimento;
import br.com.accesstage.trustion.model.TipoStatusOcorrencia;
import br.com.accesstage.trustion.repository.interfaces.IAtividadeRepository;
import br.com.accesstage.trustion.repository.interfaces.ISlaAtendimentoRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoStatusOcorrenciaRepository;
import br.com.accesstage.trustion.util.Funcoes;
import br.com.accesstage.trustion.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static br.com.accesstage.trustion.util.Utils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isNotBlank;

@Repository
public class RelatorioOcorrenciaRepository {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private IAtividadeRepository atividadeRepository;

    @Autowired
    private ITipoStatusOcorrenciaRepository tipoStatusOcorrenciaRepository;

    @Autowired
    private ISlaAtendimentoRepository slaAtendimentoRepository;

    @Log
    private static Logger LOGGER;

    /**
     * Metodo responsavel por realizar a consulta conforme sql Antes a consulta
     * utilizava uma view
     *
     * @param dto
     * @return List
     */
    public List<RelatorioOcorrenciaDTO> consulta(RelatorioOcorrenciaDTO dto) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + dto);

        List<RelatorioOcorrenciaDTO> listaRelatorioOcorrencia;
        Map<String, Object> params = new HashMap<>();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("br/com/accesstage/sql/RelatorioOcorrencia.sql");

        try {
            StringBuilder sqlQuery = new StringBuilder(Funcoes.getSqlString(in).toString());

            if (null != dto.getIdOcorrencia()) {
                sqlQuery.append(" and idOcorrencia = :idOcorrencia");
                params.put("idOcorrencia", dto.getIdOcorrencia());
            }

            if (isNotEmpty(dto.getIdsEmpresa())) {
                sqlQuery.append(" and idEmpresa in ( :idEmpresa )");
                params.put("idEmpresa", dto.getIdsEmpresa());
            }

            if (isNotEmpty(dto.getIdGrupoEconomicoList())) {
                sqlQuery.append(" and idGrupoEconomico in ( :idGrupoEconomico )");
                params.put("idGrupoEconomico", dto.getIdGrupoEconomicoList());
            }

            if (isNotEmpty(dto.getIdGrupoEconomicoOutrasEmpresasList())) {
                sqlQuery.append(" and idGrupoEconomicoOutrasEmpresas in ( :idGrupoEconomicoOutrasEmpresas )");
                params.put("idGrupoEconomicoOutrasEmpresas", dto.getIdGrupoEconomicoOutrasEmpresasList());
            }

            if (null != dto.getIdModeloNegocio()) {
                sqlQuery.append(" and idModeloNegocio = :idModeloNegocio");
                params.put("idModeloNegocio", dto.getIdModeloNegocio());
            }

            if (null != dto.getStatusOcorrencia()) {
                sqlQuery.append(" and statusOcorrencia = :statusOcorrencia");
                params.put("statusOcorrencia", dto.getStatusOcorrencia());
            }

            if (null != dto.getDataInicial()) {
                sqlQuery.append(" and dataCriacao >= :dataInicial");
                params.put("dataInicial", dto.getDataInicial());
            }

            if (null != dto.getDataFinal()) {
                sqlQuery.append(" and dataCriacao <= :dataFinal");
                params.put("dataFinal", dto.getDataFinal());
            }

            if (isNotBlank(dto.getResponsavel())) {
                sqlQuery.append(" and lower(responsavel) like :responsavel");
                params.put("responsavel", "%" + dto.getResponsavel().toLowerCase() + "%");
            }

            if (isNotEmpty(dto.getCnpjClienteList())) {
                sqlQuery.append(" and cnpjEmpresa in ( :cnpjCliente )");
                params.put("cnpjCliente", dto.getCnpjClienteList());
            }

            if (isNotBlank(dto.getCnpjCliente())) {
                sqlQuery.append(" and cnpjEmpresa = :cnpjCliente");
                params.put("cnpjCliente", dto.getCnpjCliente());
            }

            if (null != dto.getIdTransportadora()) {
                sqlQuery.append(" and idTransportadora = :idTransportadora");
                params.put("idTransportadora", dto.getIdTransportadora());
            } 
            else if(dto.getIdsTransportadoras() != null && !dto.getIdsTransportadoras().isEmpty()) {
                sqlQuery.append(" and idTransportadora in ( :idsTransportadoras )");
                params.put("idsTransportadoras", dto.getIdsTransportadoras());
            }

            if (null != dto.getGtv()) {
                sqlQuery.append(" and gtv = :gtv");
                params.put("gtv", dto.getGtv());
            }

            Query query = em.createNativeQuery(sqlQuery.toString());
            params.entrySet().stream().forEach((param) -> {
                query.setParameter(param.getKey(), param.getValue());
            });

            query.unwrap(SQLQuery.class).addScalar("rowNumber", LongType.INSTANCE).addScalar("idGrupoEconomicoOutrasEmpresas", LongType.INSTANCE).addScalar("razaoSocial", StringType.INSTANCE)
                    .addScalar("idGrupoEconomico", LongType.INSTANCE).addScalar("idEmpresa", LongType.INSTANCE).addScalar("cnpjEmpresa", StringType.INSTANCE)
                    .addScalar("idOcorrencia", LongType.INSTANCE).addScalar("statusOcorrencia", StringType.INSTANCE).addScalar("dataStatusOcorrencia", DateType.INSTANCE)
                    .addScalar("idModeloNegocio", LongType.INSTANCE).addScalar("quantidadeDiasSla", IntegerType.INSTANCE).addScalar("id_sla_atendimento", LongType.INSTANCE)
                    .addScalar("idTransportadora", LongType.INSTANCE).addScalar("razaoSocialTransportadora", StringType.INSTANCE).addScalar("gtv", StringType.INSTANCE)
                    .addScalar("dataCriacao", DateType.INSTANCE).addScalar("responsavel", StringType.INSTANCE).setResultTransformer(Transformers.aliasToBean(RelatorioOcorrenciaDTO.class));

            listaRelatorioOcorrencia = query.getResultList();

            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo RelatorioOcorrenciaRepository.consulta " + ex.getMessage());
            throw ex;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return listaRelatorioOcorrencia;
    }

    /**
     * Metodo responsavel por realizar a consulta paginada conforme sql
     *
     * @param dto
     * @param ids
     * @param pageable
     * @return Page
     */
    public Page<RelatorioOcorrenciaDTO> consultaByIds(RelatorioOcorrenciaDTO dto, List<Long> ids, Pageable pageable) {

        LOGGER.info(Utils.getInicioMetodo() + ": " + dto);

        List<RelatorioOcorrenciaDTO> listaRelatorioOcorrencia;
        Page<RelatorioOcorrenciaDTO> listaPageRelatorioOcorrencia;
        Map<String, Object> params = new HashMap<>();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("br/com/accesstage/sql/RelatorioOcorrencia.sql");
        StringBuilder sqlQuery = null;

        try {
            if (isNotEmpty(ids)) {
                sqlQuery = new StringBuilder("SELECT * from ( SELECT *, row_number() OVER ( ORDER BY id_relatorio_analitico) rowNumberFarois FROM (SELECT TOPN.*, rowNumber RNUM, COUNT(1) OVER() totalRegistrosPesquisaPorId FROM (  ");
            } else {
                sqlQuery = new StringBuilder("SELECT * FROM (SELECT TOPN.*, rowNumber RNUM FROM ( ");
            }
            
            sqlQuery.append(Funcoes.getSqlString(in).toString());

            if (null != dto.getIdOcorrencia()) {
                sqlQuery.append(" and idOcorrencia = :idOcorrencia");
                params.put("idOcorrencia", dto.getIdOcorrencia());
            }

            if (isNotEmpty(dto.getIdsEmpresa())) {
                sqlQuery.append(" and idEmpresa in ( :idEmpresa )");
                params.put("idEmpresa", dto.getIdsEmpresa());
            }

            if (isNotEmpty(dto.getIdGrupoEconomicoList())) {
                sqlQuery.append(" and idGrupoEconomico in ( :idGrupoEconomico )");
                params.put("idGrupoEconomico", dto.getIdGrupoEconomicoList());
            }

            if (isNotEmpty(dto.getIdGrupoEconomicoOutrasEmpresasList())) {
                sqlQuery.append(" and idGrupoEconomicoOutrasEmpresas in ( :idGrupoEconomicoOutrasEmpresas )");
                params.put("idGrupoEconomicoOutrasEmpresas", dto.getIdGrupoEconomicoOutrasEmpresasList());
            }

            if (null != dto.getIdModeloNegocio()) {
                sqlQuery.append(" and idModeloNegocio = :idModeloNegocio");
                params.put("idModeloNegocio", dto.getIdModeloNegocio());
            }

            if (null != dto.getStatusOcorrencia()) {
                sqlQuery.append(" and statusOcorrencia = :statusOcorrencia");
                params.put("statusOcorrencia", dto.getStatusOcorrencia());
            }

            if (null != dto.getDataInicial()) {
                sqlQuery.append(" and dataCriacao >= :dataInicial");
                params.put("dataInicial", dto.getDataInicial());
            }

            if (null != dto.getDataFinal()) {
                sqlQuery.append(" and dataCriacao <= :dataFinal");
                params.put("dataFinal", dto.getDataFinal());
            }

            if (isNotBlank(dto.getResponsavel())) {
                sqlQuery.append(" and lower(responsavel) like :responsavel");
                params.put("responsavel", "%" + dto.getResponsavel().toLowerCase() + "%");
            }

            if (isNotEmpty(dto.getCnpjClienteList())) {
                sqlQuery.append(" and cnpjEmpresa in ( :cnpjCliente )");
                params.put("cnpjCliente", dto.getCnpjClienteList());
            }

            if (isNotBlank(dto.getCnpjCliente())) {
                sqlQuery.append(" and cnpjEmpresa = :cnpjCliente");
                params.put("cnpjCliente", dto.getCnpjCliente());
            }

            if (null != dto.getIdTransportadora()) {
                sqlQuery.append(" and idTransportadora = :idTransportadora");
                params.put("idTransportadora", dto.getIdTransportadora());
            } 
            else if(dto.getIdsTransportadoras() != null && !dto.getIdsTransportadoras().isEmpty()) {
                sqlQuery.append(" and idTransportadora in ( :idsTransportadoras )");
                params.put("idsTransportadoras", dto.getIdsTransportadoras());
            }
            
            if (null != dto.getGtv()) {
                sqlQuery.append(" and gtv = :gtv");
                params.put("gtv", dto.getGtv());
            }

            if (isNotEmpty(ids)) {
                sqlQuery.append(") TOPN WHERE rowNumber in (:row_number)) RELATORIO) AS PESQUISA_POR_IDS");
                sqlQuery.append(" where rowNumberFarois <= (:pageNumber + 1) * (:pageSize) and rowNumberFarois > (:pageNumber) * (:pageSize)");
                params.put("row_number", ids);
            } else {
                sqlQuery.append(") TOPN WHERE rowNumber <= (:pageNumber + 1) * (:pageSize)) RELATORIO ");
                sqlQuery.append("WHERE RNUM > (:pageNumber) * (:pageSize)");
            }

            Query query = em.createNativeQuery(sqlQuery.toString());
            
            params.entrySet().stream().forEach((param) -> {
                query.setParameter(param.getKey(), param.getValue());
            });
            
            query.setParameter("pageNumber", pageable.getPageNumber());
            query.setParameter("pageSize", pageable.getPageSize());

            query.unwrap(SQLQuery.class).addScalar("rowNumber", LongType.INSTANCE).addScalar("idGrupoEconomicoOutrasEmpresas", LongType.INSTANCE).addScalar("razaoSocial", StringType.INSTANCE)
                    .addScalar("idGrupoEconomico", LongType.INSTANCE).addScalar("idEmpresa", LongType.INSTANCE).addScalar("cnpjEmpresa", StringType.INSTANCE)
                    .addScalar("idOcorrencia", LongType.INSTANCE).addScalar("statusOcorrencia", StringType.INSTANCE).addScalar("dataStatusOcorrencia", DateType.INSTANCE)
                    .addScalar("idModeloNegocio", LongType.INSTANCE).addScalar("quantidadeDiasSla", IntegerType.INSTANCE).addScalar("id_sla_atendimento", LongType.INSTANCE)
                    .addScalar("idTransportadora", LongType.INSTANCE).addScalar("razaoSocialTransportadora", StringType.INSTANCE).addScalar("gtv", StringType.INSTANCE)
                    .addScalar("dataCriacao", DateType.INSTANCE).addScalar("responsavel", StringType.INSTANCE).addScalar("totalRegistros", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(RelatorioOcorrenciaDTO.class));
            
            if(isNotEmpty(ids)) {
                query.unwrap(SQLQuery.class).addScalar("totalRegistrosPesquisaPorId", IntegerType.INSTANCE);
            }

            listaRelatorioOcorrencia = query.getResultList();
            Integer totalRegistros = 0;
            
            if (!listaRelatorioOcorrencia.isEmpty()) {
                
                if (isNotEmpty(ids)) {
                    totalRegistros = listaRelatorioOcorrencia.get(0).getTotalRegistrosPesquisaPorId();
                } else {
                    totalRegistros = listaRelatorioOcorrencia.get(0).getTotalRegistros();
                }
            }

            List<RelatorioOcorrenciaDTO> listaRelatorioOcorrenciaConvert = new ArrayList<>();
            listaRelatorioOcorrencia.forEach((l) -> {
                listaRelatorioOcorrenciaConvert.add(converter.convert(l));
            });
            
            listaPageRelatorioOcorrencia = new PageImpl<>(listaRelatorioOcorrenciaConvert, pageable, totalRegistros);
            LOGGER.info(Utils.getFimMetodo());

        } catch (Exception ex) {
            LOGGER.error("Ocorreu um erro no metodo RelatorioOcorrenciaRepository.consulta " + ex.getMessage());
            throw ex;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return listaPageRelatorioOcorrencia;
    }

    public Converter<RelatorioOcorrenciaDTO, RelatorioOcorrenciaDTO> converter = new Converter<RelatorioOcorrenciaDTO, RelatorioOcorrenciaDTO>() {

        @Override
        public RelatorioOcorrenciaDTO convert(RelatorioOcorrenciaDTO dto) {

            List<Atividade> atividade = atividadeRepository.findAllByIdOcorrenciaOrderByDataHorarioDesc(dto.getIdOcorrencia());

            dto.setDiasPendentes(!atividade.isEmpty() ? ChronoUnit.DAYS.between(convertToLocalDateViaSqlDate(atividade.get(0).getDataHorario()), LocalDate.now())
                    : ChronoUnit.DAYS.between(convertToLocalDateViaSqlDate(dto.getDataStatusOcorrencia()), LocalDate.now()));
            dto.setDiasEmAberto(ChronoUnit.DAYS.between(convertToLocalDateViaSqlDate(dto.getDataCriacao()), LocalDate.now()));

            TipoStatusOcorrencia statusOcorrencia = new TipoStatusOcorrencia();
            statusOcorrencia.setIdTipoStatusOcorrencia(0L);
            if (null != dto.getStatusOcorrencia()) {
                statusOcorrencia = tipoStatusOcorrenciaRepository.findByDescricao(dto.getStatusOcorrencia());
            }

            if (null != dto.getId_sla_atendimento()) {

                SlaAtendimento sla = slaAtendimentoRepository.findOne(dto.getId_sla_atendimento());

                if (statusOcorrencia.getIdTipoStatusOcorrencia().intValue() == 1) {

                    dto.setQuantidadeDiasSla(sla.getQtdDiasAnaliseSolicitada());

                    if (sla.isAnaliseSolDiaUtil()) {
                        LocalDate dtStatusOcorrencia = convertToLocalDateViaSqlDate(dto.getDataStatusOcorrencia());
                        LocalDate dateNow = LocalDate.now();
                        List<LocalDate> datesBetween = Utils.getDatesBetween(dtStatusOcorrencia, dateNow);
                        List<LocalDate> diasUteis = new ArrayList<>();

                        for (LocalDate localDate : datesBetween) {
                            if (!localDate.getDayOfWeek().name().equals(DayOfWeek.SATURDAY) && !localDate.getDayOfWeek().name().equals(DayOfWeek.SUNDAY)) {
                                diasUteis.add(localDate);
                            }
                        }

                        dto.setDiasPendentes(Long.valueOf(diasUteis.size()));

                    }

                    if (sla.getQtdDiasAnaliseSolicitada().intValue() < dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atrasado");
                    }

                    if (sla.getQtdDiasAnaliseSolicitada().intValue() == dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atenção");
                    }

                    if (sla.getQtdDiasAnaliseSolicitada().intValue() > dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Dentro do prazo");
                    }

                }

                if (statusOcorrencia.getIdTipoStatusOcorrencia().intValue() == 2) {

                    dto.setQuantidadeDiasSla(sla.getQtdDiasAnaliseAndamento());

                    if (sla.isAnaliseAndDiaUtil()) {
                        LocalDate dtStatusOcorrencia = convertToLocalDateViaSqlDate(dto.getDataStatusOcorrencia());
                        LocalDate dateNow = LocalDate.now();
                        List<LocalDate> datesBetween = Utils.getDatesBetween(dtStatusOcorrencia, dateNow);
                        List<LocalDate> diasUteis = new ArrayList<>();

                        for (LocalDate localDate : datesBetween) {
                            if (!localDate.getDayOfWeek().name().equals(DayOfWeek.SATURDAY) && !localDate.getDayOfWeek().name().equals(DayOfWeek.SUNDAY)) {
                                diasUteis.add(localDate);
                            }

                        }

                        dto.setDiasPendentes(Long.valueOf(diasUteis.size()));
                    }

                    if (sla.getQtdDiasAnaliseAndamento().intValue() < dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atrasado");
                    }

                    if (sla.getQtdDiasAnaliseAndamento().intValue() == dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atenção");
                    }

                    if (sla.getQtdDiasAnaliseAndamento().intValue() > dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Dentro do prazo");
                    }

                }

                if (statusOcorrencia.getIdTipoStatusOcorrencia().intValue() == 3) {

                    dto.setQuantidadeDiasSla(sla.getQtdDiasAnaliseAguarde());

                    if (sla.isAnaliseAguarDiaUtil()) {
                        LocalDate dtStatusOcorrencia = convertToLocalDateViaSqlDate(dto.getDataStatusOcorrencia());
                        LocalDate dateNow = LocalDate.now();
                        List<LocalDate> datesBetween = Utils.getDatesBetween(dtStatusOcorrencia, dateNow);
                        List<LocalDate> diasUteis = new ArrayList<>();

                        for (LocalDate localDate : datesBetween) {
                            if (!localDate.getDayOfWeek().name().equals(DayOfWeek.SATURDAY) && !localDate.getDayOfWeek().name().equals(DayOfWeek.SUNDAY)) {
                                diasUteis.add(localDate);
                            }
                        }

                        dto.setDiasPendentes(Long.valueOf(diasUteis.size()));

                    }

                    if (sla.getQtdDiasAnaliseAguarde().intValue() < dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atrasado");
                    }

                    if (sla.getQtdDiasAnaliseAguarde().intValue() == dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Atenção");
                    }

                    if (sla.getQtdDiasAnaliseAguarde().intValue() > dto.getDiasPendentes().intValue()) {
                        dto.setFarol("Dentro do prazo");
                    }
                }

                if (statusOcorrencia.getIdTipoStatusOcorrencia().intValue() == 4) {
                    dto.setDiasEmAberto(ChronoUnit.DAYS.between(convertToLocalDateViaSqlDate(dto.getDataCriacao()), convertToLocalDateViaSqlDate(atividade.get(0).getDataHorario())));
                    dto.setDiasPendentes(null);
                }

            }

            return dto;
        }

    };

    private LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

}
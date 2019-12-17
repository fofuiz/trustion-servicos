package br.com.accesstage.trustion.repository.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoExtratoDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoExtratoFiltroDTO;
import br.com.accesstage.trustion.model.GrupoEconomico;
import br.com.accesstage.trustion.repository.interfaces.IRelatorioAnaliticoExtratoRepository;
import br.com.accesstage.trustion.service.interfaces.IEmpresaService;
import br.com.accesstage.trustion.util.Funcoes;
import br.com.accesstage.trustion.util.ParametroQueryUtil;
import br.com.accesstage.trustion.util.Utils;

@Repository
public class RelatorioAnaliticoExtratoRepository implements IRelatorioAnaliticoExtratoRepository {
	
	@Log
	private static Logger LOGGER;
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
    private IEmpresaService empresaService;
	
	
	/** Método para retornar os ID's de grupos economicos de acordo com o usuário logado
	 * @return List<Long>
	 */
	private List<Long> carregarIdGrupoEconomico() throws Exception {
		List<Long> listDistinctIdGrupoEco = null;
		try {
			List<GrupoEconomico> listGrupos = listaGrupoEconomicoPorUsuarioLogado();
			Collection<Long> listIdGrupo = new ArrayList<>();
			listGrupos.forEach(lGp -> {
				listIdGrupo.add(lGp.getIdGrupoEconomico());
	        });
	        listDistinctIdGrupoEco = listIdGrupo.stream().distinct().collect(Collectors.toList());
			
		} catch (Exception ex) {
			LOGGER.error("Ocorreu um erro no metodo RelatorioAnaliticoExtratoRepository.carregarIdGrupoEconomico " + ex.getMessage());
			throw ex;
		}
		return listDistinctIdGrupoEco;
	}
	
	/** Método para retornar os ID's das contas de acordo com os ids dos grupos economicos
	 * @param idGrupos - List<Long>
	 * @return List<Integer>
	 */
	private List<Integer> carregarIdsContas(List<Long> idGrupos) {
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("br/com/accesstage/sql/RelatorioAnaliticoExtratoBuscarIdsContas.sql");
		List<Integer> resultListTtl;
		
		try {
			String sqlIdsContas = complementaQueryBuscarIdsContas(new StringBuilder(Funcoes.getSqlString(in).toString()).toString(), idGrupos);
			Query query = em.createNativeQuery(sqlIdsContas);
			resultListTtl = query.getResultList();
			LOGGER.info(Utils.getFimMetodo());
		} catch (Exception ex) {
			LOGGER.error("Ocorreu um erro no metodo RelatorioAnaliticoExtratoRepository.carregarIdEmpresaPorGrupoEconomico " + ex.getMessage());
			throw ex;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return resultListTtl;
	}
	
	
	/** Método para retornar a lista do relatório análitico de extrato por critério, ou seja, com filtros.
	 * @param filtroDTO - Filtro que veio do front
	 * @param pageable - Paginação para o front
	 * @return Page<RelatorioAnaliticoExtratoDTO> - Lista do Relatório Análitico de Extrato
	 */
	@Override
	public Page<RelatorioAnaliticoExtratoDTO> pesquisarPorCriterio(RelatorioAnaliticoExtratoFiltroDTO filtroDTO,
			Pageable pageable) {
		LOGGER.info(Utils.getInicioMetodo() + ": " + filtroDTO);
		
		List<Integer> listIdContas = null;
		
		try {
			List<Long> listIdGrupos = carregarIdGrupoEconomico();
			listIdContas = carregarIdsContas(listIdGrupos);
		} catch (Exception ex) {
			LOGGER.error("Ocorreu um erro na busca de Ids Contas/Grupo na classe RelatorioAnaliticoExtratoRepository " + ex.getMessage());
		}
		
		List<RelatorioAnaliticoExtratoDTO> listaRelatorioExtrato;
		Page<RelatorioAnaliticoExtratoDTO> listaPageRelatorioExtrato;

		InputStream totalIn = this.getClass().getClassLoader()
				.getResourceAsStream("br/com/accesstage/sql/RelatorioAnaliticoExtratoTotalCount.sql");
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("br/com/accesstage/sql/RelatorioAnaliticoExtrato.sql");
		
		try {
			String sqlTotalComFiltro = complementaQueryComFiltroTotal(
					new StringBuilder(Funcoes.getSqlString(totalIn).toString()).toString(), filtroDTO, pageable, listIdContas);
			String sqlProntaComFiltro = complementaQueryComFiltro(
					new StringBuilder(Funcoes.getSqlString(in).toString()).toString(), filtroDTO, pageable, listIdContas);

			Query queryTotal = em.createNativeQuery(sqlTotalComFiltro);
			Query query = em.createNativeQuery(sqlProntaComFiltro);

			query.unwrap(SQLQuery.class).addScalar("dtLancamento", DateType.INSTANCE)
					.addScalar("agencia", StringType.INSTANCE).addScalar("numDocumento", StringType.INSTANCE)
					.addScalar("natureza", StringType.INSTANCE).addScalar("valorLancamento", BigDecimalType.INSTANCE)
					.addScalar("valorCofre", BigDecimalType.INSTANCE).addScalar("loja", StringType.INSTANCE)
					.addScalar("contaCorrente", StringType.INSTANCE).addScalar("categoria", StringType.INSTANCE)
					.addScalar("historico", StringType.INSTANCE).addScalar("tipo", StringType.INSTANCE)
					.addScalar("cofre", StringType.INSTANCE).addScalar("banco", StringType.INSTANCE)
					.addScalar("status", StringType.INSTANCE).addScalar("totalRegistros", IntegerType.INSTANCE)
					.setResultTransformer(Transformers.aliasToBean(RelatorioAnaliticoExtratoDTO.class));

			listaRelatorioExtrato = query.getResultList();

			List<Integer> resultListTtl = queryTotal.getResultList();
			Integer count = resultListTtl.get(0);
			listaPageRelatorioExtrato = new PageImpl<>(listaRelatorioExtrato, pageable, count);

			LOGGER.info(Utils.getFimMetodo());

		} catch (Exception ex) {
			LOGGER.error("Ocorreu um erro no metodo RelatorioAnaliticoExtratoRepository.pesquisar " + ex.getMessage());
			throw ex;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return listaPageRelatorioExtrato;
	}

	
	/** Método para retornar a listagem de todos os registro do relatório análitico de extrato para os exportar PDF, CSV e XLS
	 * @param filtroDTO - Filtro que veio do front
	 * @return List<RelatorioAnaliticoExtratoDTO> - Lista do Relatório Análitico de Extrato
	 */
	@Override
	public List<RelatorioAnaliticoExtratoDTO> pesquisar(RelatorioAnaliticoExtratoFiltroDTO filtroDTO) {
		LOGGER.info(Utils.getInicioMetodo() + ": " + filtroDTO);
		
		List<Integer> listIdContas = null;
		try {
			List<Long> listIdGrupos = carregarIdGrupoEconomico();
			listIdContas = carregarIdsContas(listIdGrupos);
		} catch (Exception ex) {
			LOGGER.error("Ocorreu um erro na busca de Ids Empresa/Grupo na classe RelatorioAnaliticoExtratoRepository " + ex.getMessage());
		}
		
		List<RelatorioAnaliticoExtratoDTO> listaRelatorioExtratoTodos;

		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream("br/com/accesstage/sql/RelatorioAnaliticoExtratoListarTodos.sql");

		try {
			String sqlProntaComFiltro = complementaQueryComFiltroListarTodos(
					new StringBuilder(Funcoes.getSqlString(in).toString()).toString(), filtroDTO, listIdContas);

			Query query = em.createNativeQuery(sqlProntaComFiltro);

			query.unwrap(SQLQuery.class).addScalar("dtLancamento", DateType.INSTANCE)
					.addScalar("agencia", StringType.INSTANCE).addScalar("numDocumento", StringType.INSTANCE)
					.addScalar("natureza", StringType.INSTANCE).addScalar("valorLancamento", BigDecimalType.INSTANCE)
					.addScalar("valorCofre", BigDecimalType.INSTANCE).addScalar("loja", StringType.INSTANCE)
					.addScalar("contaCorrente", StringType.INSTANCE).addScalar("categoria", StringType.INSTANCE)
					.addScalar("historico", StringType.INSTANCE).addScalar("tipo", StringType.INSTANCE)
					.addScalar("cofre", StringType.INSTANCE).addScalar("banco", StringType.INSTANCE)
					.addScalar("status", StringType.INSTANCE)
					.setResultTransformer(Transformers.aliasToBean(RelatorioAnaliticoExtratoDTO.class));

			listaRelatorioExtratoTodos = query.getResultList();

			LOGGER.info(Utils.getFimMetodo());

		} catch (Exception ex) {
			LOGGER.error("Ocorreu um erro no metodo RelatorioAnaliticoExtratoRepository.pesquisar " + ex.getMessage());
			throw ex;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return listaRelatorioExtratoTodos;
	}

	
	/** Método para completar/concatenar a query usada no método de busca com filtros
	 * @param sql - Query Concatenada
	 * @param filtroDTO - Filtro que veio do front
	 * @param pageable - Paginação
	 * @param listIdsConta - Ids das contas para query
	 * @return String - Query concatenada
	 */
	private String complementaQueryComFiltro(String sql, RelatorioAnaliticoExtratoFiltroDTO filtro, Pageable pageable, List<Integer> listIdsConta) {

		StringBuilder query = new StringBuilder(sql);

		String dtInicio = convertDateToLocalDate(filtro.getDataInicial()).toString();
		String dtFinal = convertDateToLocalDate(filtro.getDataFinal()).toString();

		query.append(" WHERE (edl.data_lancamento >=" + "'" + dtInicio + "'" + " AND edl.data_lancamento <=" + "'"+ dtFinal + "'" + ") ");
		
		if (!listIdsConta.equals(null)) {
			query.append(" AND edl.conta IN ( ");
			for (int i = 0; i < listIdsConta.size(); i++) {
				query.append(listIdsConta.get(i)); 
				if (i != listIdsConta.size()-1) {
					query.append(",");
				}
			}
			query.append(" ) ");
		}

		query = filtroIf(query, filtro);

		query.append(" order by edl.data_lancamento) AS tbl WHERE totalRegistros BETWEEN (("
				+ (pageable.getPageNumber() + 1) + " - 1) * " + pageable.getPageSize() + " + 1) AND ("
				+ (pageable.getPageNumber() + 1) + " * " + pageable.getPageSize() + ")");

		return query.toString();
	}

	
	/** Método para completar/concatenar a query usada para o COUNT, para a paginação
	 * @param sql - Query Concatenada
	 * @param filtroDTO - Filtro que veio do front
	 * @param pageable - Paginação
	 * @param listIdsConta - Ids das contas para query
	 * @return String - Query concatenada
	 */
	private String complementaQueryComFiltroTotal(String sql, RelatorioAnaliticoExtratoFiltroDTO filtro, Pageable pageable, List<Integer> listIdsConta) {

		StringBuilder query = new StringBuilder(sql);

		String dtInicio = convertDateToLocalDate(filtro.getDataInicial()).toString();
		String dtFinal = convertDateToLocalDate(filtro.getDataFinal()).toString();

		query.append(" WHERE (edl.data_lancamento >=" + "'" + dtInicio + "'" + " AND edl.data_lancamento <=" + "'"+ dtFinal + "'" + ") ");
		
		if (!listIdsConta.equals(null)) {
			query.append(" AND edl.conta IN ( ");
			
			for (int i = 0; i < listIdsConta.size(); i++) {
				query.append(listIdsConta.get(i)); 
				if (i != listIdsConta.size()-1) {
					query.append(",");
				}
			}
			query.append(" ) ");
		}
		
		query = filtroIf(query, filtro);

		return query.toString();
	}

	
	/** Método para completar/concatenar a query usada para listagem de todos os registros com filtro, porém para os Exportar CSV, PDF e XLS.
	 * @param sql - Query Concatenada
	 * @param filtroDTO - Filtro que veio do front
	 * @param listIdsConta - Ids das contas para query
	 * @return String - Query concatenada
	 */
	private String complementaQueryComFiltroListarTodos(String sql, RelatorioAnaliticoExtratoFiltroDTO filtro, List<Integer> listIdsConta) {

		StringBuilder query = new StringBuilder(sql);

		String dtInicio = convertDateToLocalDate(filtro.getDataInicial()).toString();
		String dtFinal = convertDateToLocalDate(filtro.getDataFinal()).toString();

		query.append(" WHERE (edl.data_lancamento >=" + "'" + dtInicio + "'" + " AND edl.data_lancamento <=" + "'"+ dtFinal + "'" + ") ");
		
		if (!listIdsConta.equals(null)) {
			query.append(" AND edl.conta IN ( ");
			
			for (int i = 0; i < listIdsConta.size(); i++) {
				query.append(listIdsConta.get(i)); 
				if (i != listIdsConta.size()-1) {
					query.append(",");
				}
			}
			query.append(" ) ");
		}
		
		query = filtroIf(query, filtro);

		query.append(" order by edl.data_lancamento ");

		return query.toString();
	}

	
	/** Método para tratar os AND/IF das querys
	 * @param query - Query Concatenada
	 * @param filtroDTO - Filtro que veio do front
	 * @return StringBuilder - Query concatenada
	 */
	private StringBuilder filtroIf(StringBuilder query, RelatorioAnaliticoExtratoFiltroDTO filtro) {

		if (!filtro.getTransportadoras().isEmpty()) {
			query.append(" AND rac.id_transportadora IN ( ");
			
			for (int i = 0; i < filtro.getTransportadoras().size(); i++) {
				query.append(filtro.getTransportadoras().get(i).getIdTransportadora().toString());
				if (i != filtro.getTransportadoras().size()-1) {
					query.append(",");
				}
			}
			query.append(" ) ");
		}
		
		if (!filtro.getGrupoEmpresas().isEmpty()) {
			query.append(" AND rac.id_grupo_economico IN ( ");
			
			for (int i = 0; i < filtro.getGrupoEmpresas().size(); i++) {
				query.append(filtro.getGrupoEmpresas().get(i).getIdGrupoEconomico().toString());
				if (i != filtro.getGrupoEmpresas().size()-1) {
					query.append(",");
				}
			}
			query.append(" ) ");
		}
		
		if (!filtro.getEmpresas().isEmpty()) {
			query.append(" AND rac.id_empresa IN ( ");
			
			for (int i = 0; i < filtro.getEmpresas().size(); i++) {
				query.append(filtro.getEmpresas().get(i).getIdEmpresa().toString());
				if (i != filtro.getEmpresas().size()-1) {
					query.append(",");
				}
			}
			query.append(" ) ");
		}

		if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliacao().toString())) {
			query.append(" AND rac.status_conciliacao =" + "'" + filtro.getStatusConciliacao().toString() + "'");
		}

		if (ParametroQueryUtil.informadoCampo(filtro.getNumSerieCofre().toString())) {
			query.append(" AND c.num_serie =" + "'" + filtro.getNumSerieCofre().toString() + "'");
		}

//		if (ParametroQueryUtil.informadoCampo(filtro.getIdBanco().toString())) {
//			query.append(" AND edl.codigo_banco =" + filtro.getIdBanco());
//			query.append(" AND edl.agencia =" + filtro.getIdAgencia());
//		}

		return query;
	}
	
	
	/** Método para retornar query concatenada para pesquisa de contas 
	 * de acordo com os ids do grupo economico
	 * @param sql - Query concatenada
	 * @param idsGrupo - List<Long>
	 * @return String - Query Append com os IdGrupoEconomico setados
	 */
	private String complementaQueryBuscarIdsContas(String sql, List<Long> idsGrupo) {
		StringBuilder query = new StringBuilder(sql);
		query.append(" AND ge.id_grupo_economico IN ( ");
		
		for (int i = 0; i < idsGrupo.size(); i++) {
			query.append(idsGrupo.get(i).toString());
			if (i != idsGrupo.size()-1) {
				query.append(",");
			}
		}
		query.append(" ) ");
		return query.toString();
	}
	
	
	/** Método para retornar a lista de grupos economicos de acordo com o usuário
	 * @return List<GrupoEconomico>
	 */
	private List<GrupoEconomico> listaGrupoEconomicoPorUsuarioLogado() throws Exception {

        List<GrupoEconomico> lGrupos = new ArrayList<>();
        List<EmpresaDTO> listEmpresaDTO = empresaService.listaEmpresasPorUsuarioLogado();
        listEmpresaDTO.forEach(e -> {
            GrupoEconomico grupo = new GrupoEconomico();
            grupo.setIdGrupoEconomico(e.getIdGrupoEconomico());
            lGrupos.add(grupo);
        });

        return lGrupos;
    }
	
	
	/** Método para formatar um Date para LocalDate já no padrão BR
	 * @param dt - Date
	 * @return LocalDate - Valor da data formatada
	 */
	private LocalDate convertDateToLocalDate(Date dt) {
		Instant instant = Instant.ofEpochMilli(dt.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return localDateTime.toLocalDate();
	}

}
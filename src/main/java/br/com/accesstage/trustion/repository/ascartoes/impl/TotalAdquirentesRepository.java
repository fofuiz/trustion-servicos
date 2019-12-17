package br.com.accesstage.trustion.repository.ascartoes.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.internal.SessionImpl;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.model.AdquirenteDetalhe;
import br.com.accesstage.trustion.model.SkytefDetalhe;
import br.com.accesstage.trustion.util.Funcoes;

@Repository
public class TotalAdquirentesRepository {

	@Log
	private static Logger LOGGER;

	@PersistenceContext(unitName = "cartoesEntityManagerFactory")
	private EntityManager em;

	@Transactional(readOnly = true)
	public List<AdquirenteDetalhe> consultaTotalAdquirentes(Date data) {
		StringBuilder sb = new StringBuilder();

		sb.append(" SELECT t.nme_produto AS BANDEIRA, res.valor AS total ");
		sb.append(" FROM (SELECT SUM(f.vlr_bruto) AS VALOR, f.cod_produto ");
		sb.append(" FROM fato_transacao f ");
		sb.append(" WHERE f.empid =  825652 ");
		sb.append(" AND f.cod_status = 2 AND");	
		sb.append(" f.cod_data_venda =  ");
		sb.append(Funcoes.formataDataRelatorio(data));

		sb.append(" GROUP BY f.cod_produto) res, adm_produto t ");
		sb.append(" where t.cod_produto   = res.cod_produto" );
		sb.append(" order by t.nme_produto");

		LOGGER.info("Consulta total adquirentes: " + sb.toString());

		List<AdquirenteDetalhe> listaTotalAdquirentes = new ArrayList<>();

		try (Connection conn = em.unwrap(SessionImpl.class).connection();
				PreparedStatement pstm = conn.prepareStatement(sb.toString())) {

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					AdquirenteDetalhe dto = new AdquirenteDetalhe();

					dto.setBandeira(rs.getString("BANDEIRA"));
					dto.setValor(rs.getDouble("TOTAL"));

					listaTotalAdquirentes.add(dto);
				}
			} catch (SQLException ex) {
				LOGGER.error(ex.getMessage());
			}
		} catch (SQLException ex) {
			LOGGER.error(ex.getMessage());
		}
		return listaTotalAdquirentes;
	}
	
	@Transactional(readOnly = true)
	public List<SkytefDetalhe> consultaTotalSkytef(Date data) {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT  res.produto AS bandeira, SUM(res.valor) AS TOTAL FROM ");
		sb.append(" (SELECT x.dta_venda AS DATA, o.nme_operadora AS BANDEIRA, t.nme_produto AS PRODUTO, x.vlr_bruto AS VALOR ");
		sb.append(" FROM remessa_conciliacao_detalhe x, adm_operadora o, adm_produto t, adm_ponto_venda p, adm_loja j");
		sb.append(" WHERE (1 = 1) ");
		sb.append(" AND p.cod_loja = j.cod_loja ");
		sb.append(" AND x.cod_ponto_venda = p.cod_ponto_venda ");	
		sb.append(" AND x.cod_operadora = o.cod_operadora ");	
		sb.append(" AND x.cod_produto = t.cod_produto ");	
		sb.append(" AND x.empid = 825652 ");
		sb.append(" AND x.dta_venda = ");
		sb.append(Funcoes.formataDataRelatorio(data));
		sb.append(") res GROUP BY res.produto");

		LOGGER.info("Consulta total skytef: " + sb.toString());

		List<SkytefDetalhe> listaTotalSkytef = new ArrayList<>();

		try (Connection conn = em.unwrap(SessionImpl.class).connection();
				PreparedStatement pstm = conn.prepareStatement(sb.toString())) {

			try (ResultSet rs = pstm.executeQuery()) {
				while (rs.next()) {
					SkytefDetalhe dto = new SkytefDetalhe();

					dto.setBandeira(rs.getString("BANDEIRA"));
					dto.setValor(rs.getDouble("TOTAL"));

					listaTotalSkytef.add(dto);
				}
			} catch (SQLException ex) {
				LOGGER.error(ex.getMessage());
			}
		} catch (SQLException ex) {
			LOGGER.error(ex.getMessage());
		}
		return listaTotalSkytef;
	}
}
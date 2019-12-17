package br.com.accesstage.trustion.repository.mapper;

import br.com.accesstage.trustion.asconciliacao.model.DetalheConciliacaoCartaoEntity;
import br.com.accesstage.trustion.dto.conciliacao.FiltroTelasConciliacaoCartaoDetalheDTO;
import br.com.accesstage.trustion.dto.conciliacao.ResultadoConciliacaoCartaoDetalheDTO;
import br.com.accesstage.trustion.util.ParametroQueryUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DetalheConciliacaoCartaoEntityMapper {

    /**
     * metodo para complementar a query de acordo com os filtros informados.
     *
     * @param sql
     * @param filtro
     * @return query
     */
    public String complementaDetalheQueryExtrato(String sql, FiltroTelasConciliacaoCartaoDetalheDTO filtro) {

        StringBuilder query = new StringBuilder(sql);

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())) {
            query.append(" AND D.NRO_BANCO = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(" AND B.COD_OPERADORA = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliacao())) {
            query.append(" AND A.STA_CONCILIACAO = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            query.append(" AND D.NRO_AGENCIA = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            query.append(" AND D.NRO_CONTA_CORRENTE LIKE ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNomeArquivo())) {
            query.append(" AND A.NME_ARQUIVO LIKE ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getValorInicial())) {
            query.append(" AND A.VLR_EXTR_TOTAL >= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getValorFinal())) {
            query.append(" AND A.VLR_EXTR_TOTAL <= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getDataInicial())) {
            query.append(" AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'yyyymmdd') >= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getDataFinal())) {
            query.append(" AND TO_CHAR(A.DTA_EXTR_PAGAMENTO, 'yyyymmdd') <= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            query.append(" AND A.STA_CONCILIADO = ?");
        }

        if (filtro.getId() != null && ParametroQueryUtil.informadoCampo(filtro.getId().toString())) {
            query.append(" AND A.ID_FIN_CONTABIL = ?");
        }

        return query.toString();
    }

    /**
     *
     * @param filtro
     * @return
     */
    public Object[] obtemParametrosDetalheQuery(FiltroTelasConciliacaoCartaoDetalheDTO filtro) {

        Object[] parametros = new Object[obtemQtdParametrosDetalheQuery(filtro)];
        int indiceAtivo = 0;

        parametros[indiceAtivo] = filtro.getEmpId();
        indiceAtivo++;

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroBanco())) {
            parametros[indiceAtivo] = Integer.valueOf(filtro.getNumeroBanco());
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            parametros[indiceAtivo] = filtro.getCodigoOperadora();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliacao())) {
            parametros[indiceAtivo] = filtro.getStatusConciliacao();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            parametros[indiceAtivo] = Integer.valueOf(filtro.getNumeroAgencia());
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            parametros[indiceAtivo] = "%" + filtro.getNumeroConta() + "%";
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNomeArquivo())) {
            parametros[indiceAtivo] = "%" + filtro.getNomeArquivo() + "%";
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getValorInicial())) {
            parametros[indiceAtivo] = Double.parseDouble(filtro.getValorInicial().replace(".", "").replace(",", ".")) * 100;
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getValorFinal())) {
            parametros[indiceAtivo] = Double.parseDouble(filtro.getValorFinal().replace(".", "").replace(",", ".")) * 100;
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getDataInicial())) {
            parametros[indiceAtivo] = filtro.getDataInicial();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getDataFinal())) {
            parametros[indiceAtivo] = filtro.getDataFinal();
            indiceAtivo++;
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            parametros[indiceAtivo] = filtro.getStatusConciliado();
            indiceAtivo++;
        }

        if (filtro.getId() != null && ParametroQueryUtil.informadoCampo(filtro.getId().toString())) {
            parametros[indiceAtivo] = filtro.getId();
            indiceAtivo++;
        }

        return parametros;
    }

    /**
     *
     * @param filtro
     * @return
     */
    private int obtemQtdParametrosDetalheQuery(FiltroTelasConciliacaoCartaoDetalheDTO filtro) {
        //Emp Id, comeca com 1
        int paramentrosQtd = 1;

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroBanco())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliacao())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getNomeArquivo())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getValorInicial())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getValorFinal())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getDataInicial())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getDataFinal())) {
            paramentrosQtd++;
        }
        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            paramentrosQtd++;
        }
        if (filtro.getId() != null && ParametroQueryUtil.informadoCampo(filtro.getId().toString())) {
            paramentrosQtd++;
        }

        return paramentrosQtd;
    }

    /**
     * metodo para complementar a query de acordo com os filtros informados.
     *
     * @param sql
     * @param filtro
     * @return query
     */
    public String complementaDetalheQueryCartao(String sql, FiltroTelasConciliacaoCartaoDetalheDTO filtro) {

        StringBuilder query = new StringBuilder(sql);

        if (ParametroQueryUtil.informadoBancoEspecifico(filtro.getNumeroBanco())) {
            query.append(" AND D.NRO_BANCO = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getCodigoOperadora())) {
            query.append(" AND B.COD_OPERADORA = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliacao())) {
            query.append(" AND A.STA_CONCILIACAO = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroAgencia())) {
            query.append(" AND D.NRO_AGENCIA = ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNumeroConta())) {
            query.append(" AND D.NRO_CONTA_CORRENTE LIKE ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getNomeArquivo())) {
            query.append(" AND A.NME_ARQUIVO_ORIGEM LIKE ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getValorInicial())) {
            query.append(" AND VLR_LIQUIDO >= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getValorFinal())) {
            query.append(" AND VLR_LIQUIDO <= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getDataInicial())) {
            query.append(" AND A.COD_DATA_CREDITO >= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getDataFinal())) {
            query.append(" AND A.COD_DATA_CREDITO <= ?");
        }

        if (ParametroQueryUtil.informadoCampo(filtro.getStatusConciliado())) {
            query.append(" AND A.STA_CONCILIADO = ?");
        }

        return query.toString();
    }

    /**
     * metodo para converter DetalheConciliacaoCartaoEntity para
     * ResultadoConciliacaoCartaoDetalheVO
     *
     * @param listaVOs DetalheConciliacaoCartaoEntity
     * @return lista ResultadoConciliacaoCartaoDetalheVO.
     */
    public List<DetalheConciliacaoCartaoEntity> converterVOsDetalheParaEntitys(List<ResultadoConciliacaoCartaoDetalheDTO> listaVOs) {

        List<DetalheConciliacaoCartaoEntity> resultado = new ArrayList<DetalheConciliacaoCartaoEntity>();

        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);

        try {

            for (ResultadoConciliacaoCartaoDetalheDTO vo : listaVOs) {

                DetalheConciliacaoCartaoEntity entity = new DetalheConciliacaoCartaoEntity();

                entity.setId(vo.getId());
                entity.setCodOperadora(vo.getCodOperadora());
                entity.setCodEmpresa(vo.getCodEmpresa());
                entity.setCodOperadora(vo.getCodOperadora());
                entity.setCodMovimento(vo.getCodigoMovimento());
                entity.setNomeOperadora(vo.getNomeOperadora());
                entity.setNomeProduto(vo.getNomeProduto());
                entity.setNumeroBanco(vo.getNumeroBanco());
                entity.setNumeroAgencia(vo.getNumeroAgencia());
                entity.setNumeroConta(vo.getNumeroConta());
                entity.setDataExtrPagamento(vo.getDataExtrPagamento());
                entity.setValorPagamento(vo.getValor());
                entity.setStatus(vo.getStatus());
                entity.setTipoConciliacao(vo.getTipo());
                entity.setHashValue(vo.getHashValue());
                entity.setNroGrupoConciliado(vo.getNroGrupoConciliado());

                resultado.add(entity);
            }
        } catch (Exception e) {
            throw e;
        }

        return resultado;
    }

}

   SELECT  /*+ PARALLEL(f, 8) */
       COUNT(*) QUANTIDADE_ITENS_AGRUPADOS
     , f.cod_data_venda            AS DATA
     , j.nme_loja                  AS NOME_LOJA
     , t.nme_produto               AS PRODUTO
     , f.nro_plano                 AS PLANO
     , f.nro_nsu                   AS NSU
     , f.cod_autorizacao           AS AUTORIZACAO
     , SUM(f.vlr_bruto)            AS VALOR_BRUTO
     , f.cod_operadora             AS COD_OPERADORA
     , F.DSC_AREA_CLIENTE          AS DSC_AREA_CLIENTE
  from adm_ponto_venda             p
     , adm_loja                    j
     , adm_operadora               o
     , adm_produto                 t
     , fato_transacao              f
WHERE
       p.cod_loja                  = j.cod_loja(+)
   AND f.cod_ponto_venda           = p.cod_ponto_venda
   AND f.cod_produto               = t.cod_produto
   AND f.cod_operadora             = o.cod_operadora
   AND f.flg_conciliacao           IN (1,2)
   AND f.cod_status                = 2 -- Previsto
   AND f.EMPID                     IN( :empid )
   AND (f.COD_AUTORIZACAO = ? OR f.COD_AUTORIZACAO IS NULL)
   AND (f.NRO_NSU = :nroNsu OR f.NRO_NSU IS NULL)
   AND f.cod_data_venda            between :dataInicio and :dataFim
GROUP BY  f.cod_data_venda
     , j.nme_loja
     , t.nme_produto
     , f.nro_plano
     , f.nro_nsu
     , f.cod_autorizacao
     , f.cod_operadora
     , F.DSC_AREA_CLIENTE

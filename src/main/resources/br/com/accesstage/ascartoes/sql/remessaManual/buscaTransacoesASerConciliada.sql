 select  /*+ PARALLEL(x, 8) */
       x.dta_venda                 as DATA
     , j.nme_loja                  as NOME_LOJA
     , t.nme_produto               as PRODUTO
     , x.nro_plano                 as PLANO
     , x.cod_nsu                   as NSU
     , x.dsc_autorizacao           as AUTORIZACAO
     , x.vlr_bruto                 as VALOR_BRUTO
     , x.DSC_AREA_CLIENTE          as DSC_AREA_CLIENTE 
     , x.COD_DETALHE               as COD_DETALHE
  from Remessa_Conciliacao_Detalhe x
     , adm_ponto_venda             p
     , adm_loja                    j
     , adm_operadora               o
     , adm_produto                 t
 where 
       p.cod_loja                  = j.cod_loja(+)    
   and x.cod_ponto_venda           = p.cod_ponto_venda
   and x.cod_produto               = t.cod_produto
   and x.cod_operadora             = o.cod_operadora
   and x.flg_conciliacao           = 3
   and x.flg_exclusao              in (null, 0)
   AND X.COD_DETALHE               = :COD_DETALHE
   and x.empid                     = :EMPID
   and x.dta_venda                between :BEFORE and :AFTER
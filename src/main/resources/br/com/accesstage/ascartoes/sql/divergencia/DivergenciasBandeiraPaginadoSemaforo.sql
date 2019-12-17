select 
       f.cod_data_venda   as DATA
     , j.nme_loja         as LOJA
     , o.NME_EXIBICAO_PORTAL    as BANDEIRA
     , t.nme_produto      as PRODUTO
     , f.nro_plano        as PLANO
     , sum(f.vlr_bruto)   as VALOR
     , f.nro_nsu          as NSU
     , f.cod_autorizacao  as AUTORIZACAO
     , p.nro_terminal     as CAIXA
     , c.dsc_captura      as CAPTURA
     , f.cod_operadora	  as COD_OPERADORA
     , o.nme_operadora    as OPERADORA
     , f.flg_conciliacao  as STATUS
     , f.dsc_area_cliente as ID_BILHETE
     , p.cod_ponto_venda
     , f.flg_conciliacao as STATUS_CONCILIACAO
  from fato_transacao     f 
     , adm_ponto_venda    p
     , adm_loja           j
     , adm_operadora      o
     , adm_produto        t
     , dim_captura        c
 where (1 = 1)
   and p.cod_loja              = j.cod_loja(+)
   and f.cod_produto           = t.cod_produto
   and f.cod_captura           = c.cod_captura
   and f.cod_ponto_venda       = p.cod_ponto_venda   
   and f.cod_operadora         = o.cod_operadora   
   and f.flg_conciliacao       in (:flgConciliacao)
   and f.cod_status            = 2 -- CONFIRMADO
   and f.cod_data_venda        between ? and ?
   and f.empid                 in (:idsEmp)
 group 
    by f.cod_data_venda 
     , j.nme_loja
     , o.NME_EXIBICAO_PORTAL
     , t.nme_produto
     , f.nro_plano
     , f.nro_nsu
     , f.cod_autorizacao
     , p.nro_terminal
     , c.dsc_captura
     , p.cod_ponto_venda
     , f.cod_operadora
     , o.nme_operadora
     , f.flg_conciliacao
     , f.dsc_area_cliente
     , f.flg_conciliacao
 order 
    by data
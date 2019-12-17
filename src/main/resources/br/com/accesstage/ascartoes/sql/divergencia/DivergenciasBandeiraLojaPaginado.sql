select CONS.*
     , ceil(CONS.totalregistros / 75)                                                                    totalpaginas
  from (select count(1)     over()                                                                       totalregistros
     , row_number() over(order by f.cod_data_venda)   linha
     , f.cod_data_venda   as DATA
     , to_char(to_date(f.cod_data_venda, 'yyyymmdd'), 'dd/mm/yyyy') as DSC_DATA_VENDA_FORMATADA
     , j.nme_loja         as LOJA
     , o.nme_operadora    as BANDEIRA
     , t.nme_produto      as PRODUTO
     , f.nro_plano        as PLANO
     , sum(f.vlr_bruto)   as VALOR
     , f.nro_nsu          as NSU
     , f.cod_autorizacao  as AUTORIZACAO
     , p.nro_terminal     as CAIXA
     , c.dsc_captura      as CAPTURA
     , f.cod_operadora	  as COD_OPERADORA
     , o.nme_operadora    as OPERADORA
     , p.cod_ponto_venda
     , f.FLG_CONCILIACAO  as STATUS_CONCILIACAO
     , f.cod_tid_transacao				as tid_transacao
  from fato_transacao     f
     , adm_ponto_venda    p
     , adm_loja           j
     , adm_operadora      o
     , adm_produto        t
     , dim_captura        c
 where (1 = 1)
   and p.cod_loja         = j.cod_loja(+)
   and f.cod_ponto_venda  = p.cod_ponto_venda
   and f.cod_produto      = t.cod_produto
   and f.cod_operadora    = o.cod_operadora
   and f.cod_captura      = c.cod_captura
   and f.cod_data_venda   between ? and ?
   and f.flg_conciliacao       in (:flgConciliacao)
   and f.cod_status       = 2 -- CONFIRMADO
   and f.empid            in (:idsEmp)
   :pvOuLoja
   :filtroConsulta
 group
    by f.cod_data_venda
     , j.nme_loja
     , o.nme_operadora
     , t.nme_produto
     , f.nro_plano
     , f.nro_nsu
     , f.cod_autorizacao
     , p.nro_terminal
     , c.dsc_captura
     , p.cod_ponto_venda
     , f.cod_operadora
     , o.nme_operadora
     , f.FLG_CONCILIACAO
     , f.cod_tid_transacao
       ) CONS
 where linha >= ((:pPagina - 1) * 75) + 1
   and linha <= (:pPagina * 75)

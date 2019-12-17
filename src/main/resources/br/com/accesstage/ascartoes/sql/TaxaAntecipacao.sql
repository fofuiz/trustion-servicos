select nvl(l.nme_loja, v.nro_terminal)              LOJA
     , o.nme_exibicao_portal                        OPERADORA
     , p.nme_produto                                PRODUTO
     , f.nro_plano                                  PLANO
     , (100 * sum(f.vlr_taxa_antecipacao)) / sum(f.vlr_bruto - f.vlr_comissao)    TAXA
  from fato_transacao_sum  f
     , adm_produto     p
     , adm_loja        l
     , adm_ponto_venda v
     , adm_operadora   o
 where f.cod_produto     = p.cod_produto
   and f.cod_natureza    = 1 -- 'PARCELA'
   and f.cod_ponto_venda = v.cod_ponto_venda
   and f.cod_operadora   = o.cod_operadora
   and v.cod_loja        = l.cod_loja (+)
   and f.cod_status      IN (3, 8) -- LIQUIDADO / ANTECIPADO CIELO
   and f.flg_antecipado  = 1
   and f.empid           in (:idsEmp)
   and f.cod_data_credito  between :dataInicial and :dataFinal
 group 
    by nvl(l.nme_loja, v.nro_terminal)
     , o.nme_exibicao_portal
     , p.nme_produto
     , f.nro_plano
 order 
    by nvl(l.nme_loja, v.nro_terminal)
     , o.nme_exibicao_portal
     , p.nme_produto
     , f.nro_plano	   

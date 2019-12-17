select *
    from (
        select t.* 
            , count(1) over() totalRegistros
            , row_number() over(order by t.loja, t.operadora, t.Produto, t.Plano)   linha
        from (
            select TOPN.*
                   , case when (round(abs(TOPN.taxa - (100 * TOPN.txCadastrada)), 2) <= 0.05) then 'C' else 'D' end tipo
            from (
                select 
                    l.nme_loja                                     as  loja
                    , v.nro_terminal                               as  pontoVenda
                    , o.nme_exibicao_portal                        as  operadora
                    , p.nme_produto                                as  produto
                    , f.nro_plano                                  as  plano
                    , round((100 * sum(f.vlr_comissao)) / sum(case f.vlr_bruto when 0 then 0.001 else f.vlr_bruto end),2) as taxa
                    , t.tx_adm_cadastrada                          as txCadastrada
                from 
                    fato_transacao_sum  f
                    , adm_produto     p
                    , adm_loja        l
                    , adm_ponto_venda v
                    , adm_Operadora   o
                    , adm_taxa_admin  t
                where 
                    f.cod_produto = p.cod_produto
                    and f.cod_natureza    = 1  -- 'PARCELA'
                    and f.cod_ponto_venda = v.cod_ponto_venda
                    and f.cod_operadora   = o.cod_operadora
                    and v.cod_loja        = l.cod_loja (+)
                    and f.cod_status      = 2 -- CONFIRMADO
                    and f.cod_operadora   = t.cod_operadora(+)
                    and f.cod_produto     = t.cod_produto(+)
                    and f.nro_plano       = t.nro_plano(+)
                    and f.empid           = t.empid(+)
                    and f.cod_ponto_venda = t.cod_ponto_venda(+)
                    and f.empid           in (:idsEmp)
                    and f.cod_data_venda  between :dataInicial and :dataFinal
                    :pvOuLoja
                group by
                  l.nme_loja
                  , v.nro_terminal
                  , o.nme_exibicao_portal
                  , p.nme_produto
                  , f.Nro_Plano
                  , T.Tx_Adm_Cadastrada
                order by
                  l.nme_loja
                  , v.nro_terminal
                  , o.nme_exibicao_portal
                  , p.Nme_Produto
                  , f.Nro_Plano
                ) TOPN ) t
            where 
                tipo in  (:tipo) ) x
    where 
        linha >= ((:pageNumber - 1) * (:pageSize)) + 1
        and linha <= (:pageNumber * (:pageSize))
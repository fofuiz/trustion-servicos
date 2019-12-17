select a.vlr_bruto            VALOR_BRUTO
     , a.vlr_liquido          VALOR_LIQUIDO
     , a.vlr_taxa_antecipacao VALOR_TXANTECIPACAO
     , a.nro_nsu              NSU
     , a.cod_autorizacao      AUTORIZACAO
     , a.nro_parcela          PARCELA
     , a.nro_plano            PLANO
     , a.flg_antecipado       E_ANTECIPADO
     , a.cod_operadora        DIM_BANDEIRA
     , a.cod_data_venda       DATA_VENDA
     , a.cod_numerocartao     NUMERO_CARTAO
     , a.cod_data_credito     DATA_CREDITO
     , cb.nro_agencia         NRO_AGENCIA
     , cb.nro_banco           NRO_BANCO
     , cb.nro_conta_corrente  NRO_CONTA_CORRENTE
     , a.cod_lote_bandeira    LOTE_BANDEIRA
     , p.nme_produto          PRODUTO
     , l.nme_loja             LOJA
     , v.nro_terminal         PV
     , a.cod_status           COD_STATUS
     , op.sgl_operadora       SGLOPERADORA
     , a.cod_tid_transacao    TID
     , case when a.flg_conciliacao <> 0 then 'SIM' else 'NAO' end CONCILIADA
     , a.nro_logico_terminal NUMLOGICO
     , a.dsc_area_cliente    IDCONCILIACAO
     , a.vlr_comissao        VALOR_TXADMIN
     , a.cod_data_reagendamento           DATA_REAGENDAMENTO
     , s.dsc_status                       DSC_STATUS
     , op.nme_exibicao_portal             NME_OPERADOR_EXIBICAO_PORTAL
  from fato_transacao a
     , dim_conta_banco cb
     , adm_produto     p
     , adm_loja        l
     , adm_ponto_venda v
     , adm_operadora   op
     , dim_status      s
where a.cod_conta_banco       = cb.cod_conta_banco(+)
   and v.cod_loja              = l.cod_loja(+)
   and a.cod_produto           = p.cod_produto
   and a.cod_ponto_venda       = v.cod_ponto_venda
   and a.cod_operadora         = op.cod_operadora
   and a.cod_status            = s.cod_status
   and a.empid                 in (:idsEmp)
   and a.cod_data_venda        = ?
   and (a.nro_nsu              = ? or a.nro_nsu               is null)
   and (a.cod_autorizacao      = ? or a.cod_autorizacao       is null)
 order
    by parcela
     , cod_status --desc
select pv.nro_terminal             NRO_TERMINAL
     , l.nme_loja                  NME_LOJA
     , f.cod_lote_bandeira         DSC_LOTE
     , f.nro_parcela               NRO_PARCELA         
     , f.nro_plano                 NRO_PLANO 
     , f.nro_nsu                   NRO_NSU             
     , f.cod_autorizacao           COD_AUTORIZACAO  
     , o.sgl_operadora             SGL_OPERADORA
     , f.cod_data_venda            DTA_VENDA
     , f.cod_data_credito          DTA_CREDITO
     , f.vlr_bruto                 VLR_BRUTO  
     , f.vlr_comissao              VLR_COMISSAO 
     , f.vlr_liquido               VLR_LIQUIDO 
     , p.nme_produto               DSC_PRODUTO
     , o.nme_exibicao_portal       DSC_OPERADORA 
     , cb.nro_banco                NRO_BANCO
     , cb.nro_agencia              NRO_AG
     , ltrim(cb.nro_conta_corrente, '0') NRO_CC
     , f.dsc_area_cliente          DSC_AREA_CLIENTE
     , f.cod_natureza              COD_NATUREZA 
  from fato_transacao              f
     , adm_loja                    l
     , adm_ponto_venda             pv
     , adm_produto                 p
     , dim_conta_banco             cb
     , adm_operadora               o
 where pv.cod_loja                 = l.cod_loja(+)
   and f.cod_ponto_venda           = pv.cod_ponto_venda
   and f.cod_produto               = p.cod_produto
   and f.cod_conta_banco           = cb.cod_conta_banco
   and f.cod_operadora             = o.cod_operadora
   and f.cod_status                = 2 /* CONFIRMADO */   
   and f.flg_conciliacao           <> 0   
   and f.cod_data_venda            between ? and ?
   and f.empid                     in (:idsEmp)
   :pvOuLoja
 order 
    by f.cod_natureza
     , dsc_lote
     , nro_nsu
     , nro_plano
     , nro_parcela

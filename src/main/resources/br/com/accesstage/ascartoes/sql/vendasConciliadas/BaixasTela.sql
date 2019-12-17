select sum(f.vlr_bruto)            VLR_BRUTO           
     , f.nro_nsu                   NRO_NSU             
     , f.cod_autorizacao           COD_AUTORIZACAO
     , f.nro_parcela               NRO_PARCELA
     , f.nro_plano                 NRO_PLANO     
     , f.cod_natureza              COD_NATUREZA    
     , f.cod_data_venda            DTA_VENDA
     , f.vlr_comissao              VLR_COMISSAO
     , f.vlr_taxa_antecipacao      VLR_TAXA_ANTECIPACAO
     , l.nme_loja                  NME_LOJA
     , p.idt_produto               IDT_PRODUTO
     , p.nme_produto               DSC_PRODUTO
     , o.NME_EXIBICAO_PORTAL       DSC_OPERADORA
     , cb.nro_banco                NRO_BANCO
     , cb.nro_agencia              NRO_AG
     , ltrim(cb.nro_conta_corrente, '0') NRO_CC     
     , pv.cod_ponto_venda
  from fato_transacao              f
     , adm_loja                    l
     , adm_ponto_venda             pv
     , adm_produto                 p
     , dim_conta_banco             cb
     , adm_operadora               o
 where pv.cod_loja                 = l.cod_loja(+)
   and f.cod_ponto_venda           = pv.cod_ponto_venda
   and f.cod_produto               = p.cod_produto
   and f.cod_operadora             = o.cod_operadora
   and f.cod_conta_banco           = cb.cod_conta_banco
   and f.cod_status                = 2 /* CONFIRMADO */   
   and f.flg_conciliacao           <> 0   
   and f.cod_data_venda            between ? and ?
   And F.Empid                     In (:idsEmp)
   :pvOuLoja
 group 
    by  
     f.nro_nsu                   
     , f.cod_autorizacao     
     , f.nro_plano   
     , f.cod_natureza
     , f.cod_data_venda    
     , f.nro_parcela
     , f.vlr_comissao
     , f.vlr_taxa_antecipacao
     , l.nme_loja                  
     , p.idt_produto               
     , P.Nme_Produto               
     , o.NME_EXIBICAO_PORTAL
     , pv.cod_ponto_venda
     , cb.nro_banco
     , cb.nro_agencia
     , cb.nro_conta_corrente
 order
    by DTA_VENDA     
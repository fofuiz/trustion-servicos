select distinct frc.vlr_bruto                 VLR_BRUTO    
     , frc.vlr_bruto - ((round(frc.vlr_bruto/100 * (1 - ((f.vlr_liquido+f.vlr_taxa_antecipacao) / f.vlr_bruto)),2)) * 100)               VLR_LIQUIDO         
     , (round(frc.vlr_bruto/100 * (1 - ((f.vlr_liquido+f.vlr_taxa_antecipacao) / f.vlr_bruto)),2)) * 100 VLR_COMISSAO
	 , (round((frc.vlr_bruto - ((round(frc.vlr_bruto/100 * (1 - ((f.vlr_liquido+f.vlr_taxa_antecipacao) / f.vlr_bruto)),2)) * 100))/100 * (1 - ((f.vlr_liquido+f.vlr_comissao) / f.vlr_bruto)),2)) * 100 as VLR_TAXA_ANTECIPACAO
     , f.nro_nsu                   NRO_NSU             
     , f.cod_autorizacao           COD_AUTORIZACAO     
     , f.nro_parcela               NRO_PARCELA         
     , f.nro_plano                 NRO_PLANO           
     , f.cod_natureza              COD_NATUREZA        
     , f.cod_data_venda            DTA_VENDA
     , f.cod_data_credito          DTA_CREDITO
     , cb.nro_banco                NRO_BANCO
     , ltrim(cb.nro_conta_corrente, '0')       NRO_CC
     , cb.nro_agencia              NRO_AG
     , p.idt_bandeira              IDT_OPERADORA     
     , l.nme_loja                  NME_LOJA
     , p.idt_produto               IDT_PRODUTO
     , f.cod_lote_bandeira         DSC_LOTE
     , f.dsc_area_cliente          DDS_CLIENTE
     , f.cod_numerocartao          DSC_NUMEROCARTAO
     , pv.cod_ponto_venda          COD_PONTO_VENDA
     , p.cod_produto_exportacao    COD_PRODUTO
     , o.cod_operadora_exportacao  COD_OPERADORA
     , frc.dsc_area_cliente        DSC_AREA_CLIENTE
     , o.sgl_operadora            SGL_OPERADORA
     , pv.nro_terminal             NRO_TERMINAL
  from fato_transacao              f
     , adm_loja                    l
     , adm_ponto_venda             pv
     , adm_produto                 p
     , dim_conta_banco             cb
     , adm_operadora               o
     , fato_remessa_conciliacao    frc
 where pv.cod_loja                 = l.cod_loja(+)
   and f.hash_value                = frc.hash_value
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
     , DSC_AREA_CLIENTE
     , nro_plano
     , nro_parcela

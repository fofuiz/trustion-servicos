select sum(f.vlr_bruto)                 VLR_BRUTO    
     , f.nro_nsu                   NRO_NSU             
     , f.cod_autorizacao           COD_AUTORIZACAO     
     , f.nro_plano                 NRO_PLANO           
     , f.cod_data_venda            DTA_VENDA
     , l.nme_loja                  NME_LOJA
     , P.Idt_Produto               IDT_PRODUTO
     , F.Cod_Operadora
     , F.cod_natureza
     , pv.cod_ponto_venda   COD_PONTO_VENDA
     , p.nme_produto               DSC_PRODUTO
     , o.NME_EXIBICAO_PORTAL       DSC_OPERADORA
  from fato_transacao              f
     , adm_loja                    l
     , adm_ponto_venda             pv
     , adm_produto                 p
     , Dim_Conta_Banco             Cb
     , adm_operadora               o
 Where Pv.Cod_Loja                 = L.Cod_Loja(+)
   and f.cod_operadora             = o.cod_operadora
   and f.cod_ponto_venda           = pv.cod_ponto_venda
   and f.cod_produto               = p.cod_produto
   and f.cod_conta_banco           = cb.cod_conta_banco
   and f.cod_status                = 2 /* CONFIRMADO */   
   And F.Flg_Conciliacao           <> 0
   and f.cod_data_venda            between ? and ?
   And F.Empid                     In (:idsEmp)
   :pvOuLoja
 Group 
    By 
     f.nro_nsu       
     , f.cod_autorizacao
     , f.nro_plano      
     , f.cod_data_venda 
     , l.nme_loja       
     , P.Idt_Produto    
     , F.Cod_Operadora
     , F.cod_natureza
     , pv.cod_ponto_venda
     , p.nme_produto
     , o.NME_EXIBICAO_PORTAL
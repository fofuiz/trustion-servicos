   SELECT  /*+ PARALLEL(f, 8) */        
       f.cod_data_venda            AS DATA
     , j.cod_loja                  AS COD_LOJA
     , j.nme_loja                  AS NOME_LOJA
     , t.nme_produto               AS PRODUTO
     , f.nro_plano                 AS PLANO
     , f.nro_nsu                   AS NSU
     , f.cod_autorizacao           AS AUTORIZACAO
     , f.cod_operadora             AS COD_OPERADORA
     , F.DSC_AREA_CLIENTE          AS DSC_AREA_CLIENTE 
     , f.vlr_bruto                 AS VALOR_BRUTO
     , f.hash_value                AS HASH_VALUE     
  from adm_ponto_venda             p
     , adm_loja                    j
     , adm_operadora               o
     , adm_produto                 t
     , fato_transacao              f
WHERE 
       p.cod_loja                  = j.cod_loja(+)    
   AND f.cod_ponto_venda           = p.cod_ponto_venda
   AND f.cod_produto               = t.cod_produto
   AND f.cod_operadora             = o.cod_operadora
   AND f.flg_conciliacao           = 0
   AND f.cod_status                IN ( 2) -- Previsto
   AND f.empid                     = :EMPID
   AND f.cod_operadora             = :CODOPERADORA
   AND f.nro_plano                 = :NROPLANO
   AND t.nme_produto               = :NMEPRODUTO
   AND F.COD_AUTORIZACAO = :COD_AUTORIZACAO   
   AND f.cod_data_venda                between :BEFORE and :AFTER

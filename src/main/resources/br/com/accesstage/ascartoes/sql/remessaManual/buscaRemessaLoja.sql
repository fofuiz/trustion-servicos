SELECT RCD.DTA_VENDA AS DTVENDA,
       RCD.COD_PRODUTO AS CODPRODUTO,
       APROD.NME_PRODUTO AS PRODUTO,
       RCD.NRO_PLANO AS NROPLANO,
       RCD.VLR_BRUTO AS VLRBRUTO,
       RCD.COD_NSU AS NSU,
       RCD.DSC_AUTORIZACAO AS AUTORIZACAO,
       RCD.COD_DETALHE AS COD_DETALHE,
       RCD.DSC_AREA_CLIENTE AS DSCAREACLIENTE,
       RCD.COD_OPERADORA
  FROM REMESSA_CONCILIACAO_DETALHE RCD
       , ADM_PONTO_VENDA APV
       , ADM_LOJA AL
       , ADM_PRODUTO APROD
  WHERE 
        rcd.cod_ponto_venda = apv.cod_ponto_venda
    AND apv.cod_loja        = al.cod_loja
    AND rcd.cod_produto     = aprod.cod_produto
    AND al.cod_loja         = :CODLOJA
    AND rcd.empid           = :EMPID
    AND RCD.STA_CONCILIACAO = 'EM_EDICAO'
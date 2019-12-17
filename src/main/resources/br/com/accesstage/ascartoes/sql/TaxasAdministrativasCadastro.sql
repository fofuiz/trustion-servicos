SELECT 
    t.cod_taxa_admin AS codTaxaAdministrativa, 
    t.empid AS codEmp, 
    t.cod_ponto_venda AS codPontoVenda, 
    t.cod_operadora AS codOperadora, 
    t.cod_produto AS codProduto, 
    t.nro_plano AS nroPlano, 
    t.tx_adm_cadastrada AS txAdmCadastrada,
    t.cod_usuario AS codUsuario,    
    l.nme_loja AS nmeLoja,
    p.nro_terminal AS nmePontoVenda, 
    o.nme_operadora AS nmeOperadora, 
    pr.nme_produto AS nome
FROM adm_taxa_admin t,     
    adm_ponto_venda p, 
    adm_operadora o, 
    adm_produto pr,
    adm_loja l
WHERE t.cod_ponto_venda = p.cod_ponto_venda (+)
    AND p.cod_loja = l.cod_loja (+)
    AND t.cod_operadora = o.cod_operadora 
    AND t.cod_produto = pr.cod_produto
    AND p.nro_terminal not like 'SLVLoja%'

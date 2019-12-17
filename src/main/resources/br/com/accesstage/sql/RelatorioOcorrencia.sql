SELECT 
row_number() OVER ( ORDER BY id_relatorio_analitico) rowNumber, 
*, 
COUNT(1) OVER() totalRegistros FROM 
(
SELECT DISTINCT ra.id_relatorio_analitico,
emp.id_grupo_economico idGrupoEconomicoOutrasEmpresas, 
emp.razao_social razaoSocial, 
ra.id_grupo_economico idGrupoEconomico, 
ra.id_empresa idEmpresa, 
ra.cnpj_cliente cnpjEmpresa, 
ra.id_ocorrencia idOcorrencia, 
ra.status_ocorrencia statusOcorrencia, 
ra.data_status_ocorrencia dataStatusOcorrencia, 
remp.id_modelo_negocio idModeloNegocio, 
mn.qtd_dias_conferencia_credito quantidadeDiasSla, 
sla.id_sla_atendimento id_sla_atendimento, 
ocor.data_criacao dataCriacao, 
( SELECT atv.responsavel 
  FROM t_atividade atv 
  WHERE atv.id_ocorrencia = ra.id_ocorrencia 
  AND atv.data_criacao = ( 
	( SELECT max(atv_1.data_criacao) AS max
	  FROM t_atividade atv_1 
	  WHERE atv_1.id_ocorrencia = ra.id_ocorrencia )
	) 
) responsavel,
ra.id_transportadora idTransportadora,
tra.razao_social razaoSocialTransportadora,
NULL gtv
FROM  t_relatorio_analitico_credito ra 
LEFT JOIN t_empresa emp ON ra.id_empresa = emp.id_empresa 
LEFT JOIN t_empresa_modelo_negocio remp on emp.id_empresa = remp.id_empresa 
LEFT JOIN t_sla_atendimento sla ON remp.id_modelo_negocio = sla.id_modelo_negocio 
LEFT JOIN t_modelo_negocio mn ON remp.id_modelo_negocio = mn.id_modelo_negocio 
LEFT JOIN t_ocorrencia ocor ON ra.id_ocorrencia = ocor.id_ocorrencia 
LEFT JOIN t_transportadora tra ON ra.id_transportadora = tra.id_transportadora

UNION ALL

SELECT distinct ra.id_relatorio_analitico,
emp.id_grupo_economico idGrupoEconomicoOutrasEmpresas, 
emp.razao_social razaoSocial, 
ra.id_grupo_economico idGrupoEconomico, 
ra.id_empresa idEmpresa, 
ra.cnpj cnpjEmpresa, 
ra.id_ocorrencia idOcorrencia, 
ra.status_ocorrencia statusOcorrencia, 
ra.dt_status_ocorrencia dataStatusOcorrencia, 
remp.id_modelo_negocio idModeloNegocio, 
mn.qtd_dias_conferencia_credito quantidadeDiasSla, 
sla.id_sla_atendimento id_sla_atendimento, 
ocor.data_criacao dataCriacao, 
( SELECT atv.responsavel 
  FROM t_atividade atv 
  WHERE atv.id_ocorrencia = ra.id_ocorrencia 
  AND atv.data_criacao = ( 
	( SELECT max(atv_1.data_criacao) AS max
	  FROM t_atividade atv_1 
	  WHERE atv_1.id_ocorrencia = ra.id_ocorrencia )
	) 
) responsavel,
ra.id_transportadora idTransportadora,
tra.razao_social razaoSocialTransportadora,
ra.gtv gtv
FROM  t_relatorio_analitico_credito_d1 ra 
LEFT JOIN t_empresa emp ON ra.id_empresa = emp.id_empresa 
LEFT JOIN t_empresa_modelo_negocio remp on emp.id_empresa = remp.id_empresa 
LEFT JOIN t_sla_atendimento sla ON remp.id_modelo_negocio = sla.id_modelo_negocio 
LEFT JOIN t_modelo_negocio mn ON remp.id_modelo_negocio = mn.id_modelo_negocio 
LEFT JOIN t_ocorrencia ocor ON ra.id_ocorrencia = ocor.id_ocorrencia 
LEFT JOIN t_transportadora tra ON ra.id_transportadora = tra.id_transportadora

) AS relatorio 

WHERE idOcorrencia IS NOT NULL
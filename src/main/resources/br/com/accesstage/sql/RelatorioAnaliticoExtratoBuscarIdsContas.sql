select distinct 
	db.conta conta
from t_grupo_economico ge
	join t_empresa emp
		on ge.id_grupo_economico = emp.id_grupo_economico
	join t_dados_bancarios db
		on db.id_empresa = emp.id_empresa
where emp.status = 'Ativo' 
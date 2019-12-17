SELECT cast( count(*) as int) FROM t_ext_dtl_lote edl
		LEFT JOIN t_ext_elegivel el
			ON el.historico_lancamento = edl.historico_lancamento
			AND el.valor_lancamento = edl.valor_lancamento
			AND cast(el.cnpj as int8) = edl.numero_inscricao
			AND edl.data_lancamento = el.data_lancamento
		LEFT JOIN t_relatorio_analitico_credito rac
			ON rac.id_conciliacao = el.id_conciliacao
		LEFT JOIN t_cofre c 
			ON c.id_empresa = rac.id_empresa
		LEFT join t_lista_banco	bc
			ON cast(bc.codigo_banco as int8) = edl.codigo_banco 
		left join t_empresa emp 
			on emp.id_empresa = rac.id_empresa
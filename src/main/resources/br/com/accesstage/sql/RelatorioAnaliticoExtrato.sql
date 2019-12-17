SELECT * FROM (
		SELECT ROW_NUMBER() OVER(ORDER BY edl.data_lancamento) AS totalRegistros, 
		    edl.data_lancamento dtLancamento,
		    edl.agencia agencia,
		    edl.numero_documento numDocumento,
		    edl.natureza_lancamento natureza, 
		    edl.valor_lancamento valorLancamento,
		    rac.valor_total valorCofre,
		    emp.razao_social loja,
		    el.conta contaCorrente,
			edl.categoria_lancamento categoria,
			edl.historico_lancamento historico,
			CASE
			    WHEN edl.tipo_lancamento = 'C' THEN 'CRÉDITO'
			    ELSE 'DÉBITO'
			END AS tipo,	
			rac.num_serie_equipamento cofre,
		    bc.descricao banco,
			rac.status_conciliacao status
		FROM t_ext_dtl_lote edl
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

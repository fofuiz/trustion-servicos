package br.com.accesstage.trustion.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import br.com.accesstage.trustion.dto.DetalheRelatorioDTO;
import br.com.accesstage.trustion.model.DetalheRelatorio;

public class DetalheRelatorioConverter {

	public static DetalheRelatorioDTO paraDTO(DetalheRelatorio entidade) {

		DetalheRelatorioDTO dto = new DetalheRelatorioDTO();

		dto.setIdDetalheRelatorio(entidade.getIdDetalheRelatorio());
		dto.setNumSerie(entidade.getNumSerie());
		dto.setDepositoDT(entidade.getDepositoDT()== null ? null : Date.from(entidade.getDepositoDT().atZone(ZoneId.systemDefault()).toInstant()));
		dto.setCodigoMovimento(entidade.getCodigoMovimento());
		dto.setDepositante(entidade.getDepositante());
		dto.setTipoDeposito(entidade.getTipoDeposito());
		dto.setCodigoFechamento(entidade.getCodigoFechamento());
		dto.setSequencia(entidade.getSequencia());
		dto.setValorTotal(entidade.getValorTotal());
		dto.setDataCriacao(entidade.getDataCriacao()== null ? null : Date.from(entidade.getDataCriacao().atZone(ZoneId.systemDefault()).toInstant()));
		dto.setIdEquipamento(entidade.getIdEquipamento());

		return dto;
	}
	
	public static DetalheRelatorio paraEntidade(DetalheRelatorioDTO dto) {

		DetalheRelatorio entidade = new DetalheRelatorio();

		entidade.setIdDetalheRelatorio(dto.getIdDetalheRelatorio());
		entidade.setNumSerie(dto.getNumSerie());
		entidade.setDepositoDT(dto.getDepositoDT()== null ? null : LocalDateTime.ofInstant(dto.getDepositoDT().toInstant(), ZoneId.systemDefault()));
		entidade.setCodigoMovimento(dto.getCodigoMovimento());
		entidade.setDepositante(dto.getDepositante());
		entidade.setTipoDeposito(dto.getTipoDeposito());
		entidade.setCodigoFechamento(dto.getCodigoFechamento());
		entidade.setSequencia(dto.getSequencia());
		entidade.setValorTotal(dto.getValorTotal());
		entidade.setDataCriacao(dto.getDataCriacao()== null ? null : LocalDateTime.ofInstant(dto.getDataCriacao().toInstant(), ZoneId.systemDefault()));
		entidade.setIdEquipamento(dto.getIdEquipamento());

		return entidade;
	}
}

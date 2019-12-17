package br.com.accesstage.trustion.dto;

import java.time.LocalDate;
import java.util.Date;

import br.com.accesstage.trustion.model.ConciliacaoVendasPac;
import org.springframework.data.domain.Page;

import br.com.accesstage.trustion.model.VideoGTV;
import lombok.Data;

import javax.persistence.*;

@Data
public class ConciliacaoVendasNumerarioDTO {

	private int id;
	private LocalDate dataConciliacao;
	private LocalDate dataInicioConciliacao;
	private LocalDate dataFimConciliacao;
	private boolean conciliadoComSucesso;
	private int idLoja;
	private Long numeroGtv;
	private double somatoriaVendas;
	private double pacVlrDeclarado;
	private double diferenca;
	private String siglaLoja;
	private LocalDate datagtv;
	private int qtdColSemana;
	private String razaoSocial;
	private String cnpj;
	private double pacVlrConferido;
	private  Long linkVideos;
	
	public ConciliacaoVendasNumerarioDTO() {
		
	}
	
	public ConciliacaoVendasNumerarioDTO(ConciliacaoVendasPac conciliacaoVendasPac) {
		this.id = conciliacaoVendasPac.getId();
		this.dataConciliacao = conciliacaoVendasPac.getDataConciliacao();
		this.dataInicioConciliacao = conciliacaoVendasPac.getDataInicioConciliacao();
		this.dataFimConciliacao = conciliacaoVendasPac.getDataFimConciliacao();
		this.conciliadoComSucesso = conciliacaoVendasPac.isConciliadoComSucesso();
		this.idLoja = conciliacaoVendasPac.getIdLoja();
		this.numeroGtv = conciliacaoVendasPac.getNumeroGtv();
		this.somatoriaVendas = conciliacaoVendasPac.getSomatoriaVendas();
		this.pacVlrDeclarado = conciliacaoVendasPac.getPacVlrDeclarado();
		this.diferenca = conciliacaoVendasPac.getDiferenca();
		this.siglaLoja = conciliacaoVendasPac.getSiglaLoja();
		this.datagtv = conciliacaoVendasPac.getDatagtv();
		this.qtdColSemana = conciliacaoVendasPac.getQtdColSemana();
		this.razaoSocial = conciliacaoVendasPac.getRazaoSocial();
		this.cnpj = conciliacaoVendasPac.getCnpj();
		this.pacVlrConferido = conciliacaoVendasPac.getPacVlrConferido();
		this.linkVideos = conciliacaoVendasPac.getNumeroGtv();
	}
	
	//Converter
	public static Page<ConciliacaoVendasNumerarioDTO> converter(Page<ConciliacaoVendasPac> listaVideosGTV) {
		return listaVideosGTV.map(ConciliacaoVendasNumerarioDTO::new);
	}

}

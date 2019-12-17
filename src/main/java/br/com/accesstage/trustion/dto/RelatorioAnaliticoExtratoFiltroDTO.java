package br.com.accesstage.trustion.dto;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

import br.com.accesstage.trustion.enums.StatusConciliacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioAnaliticoExtratoFiltroDTO {
	
	private List<TransportadoraDTO> transportadoras;
	
	private List<GrupoEconomicoDTO> grupoEmpresas;
	
	private List<EmpresaDTO> empresas;
	
	//private CofreDTO cofre;
	private String numSerieCofre;
	
	private String statusConciliacao;
	
	//private DadosBancariosDTO dadosBancarios;
	private String idBanco;
	private String idAgencia;
	
	@NotNull
    private Date dataInicial;
	
    @NotNull
    private Date dataFinal;
    

	public List<EmpresaDTO> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<EmpresaDTO> empresas) {
		this.empresas = empresas;
	}

    
    
    
    
    //Getter's and Setter's
    /*
	public CofreDTO getCofre() {
		if (cofre.equals(null)) {
			this.cofre = new CofreDTO();
			return this.cofre;
		}else {
			return this.cofre;
		}
	}

	public void setCofre(CofreDTO cofre) {
		if (cofre == null) {
			this.cofre = new CofreDTO();
		}else {
			this.cofre = cofre;
		}
		
	}
	
	public DadosBancariosDTO getDadosBancarios() {
		if (dadosBancarios.equals(null)) {
			DadosBancariosDTO obj = new DadosBancariosDTO();
			obj.setIdBanco(0l);
			this.dadosBancarios = obj;
			return this.dadosBancarios;
		}else {
			return this.dadosBancarios;
		}
	}
	
	public void setDadosBancarios(DadosBancariosDTO dadosBancarios) {
		if (dadosBancarios == null) {
			DadosBancariosDTO obj = new DadosBancariosDTO();
			obj.setIdBanco(0l);
			this.dadosBancarios = obj;
		}else {
			this.dadosBancarios = dadosBancarios;
		}
	}*/
	

}

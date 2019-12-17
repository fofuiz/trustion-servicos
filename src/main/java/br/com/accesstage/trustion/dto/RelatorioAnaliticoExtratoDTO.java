package br.com.accesstage.trustion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RelatorioAnaliticoExtratoDTO {

	//Transportadora
    private Long idTransportadora;
    private String transportadora;
    private List<TransportadoraDTO> transportadoras;    
    
    //Grupo Economico
    private Long idGrupoEconomico;
    private String grupoEconomico;
    private List<GrupoEconomicoDTO> outrasEmpresas;

    
    //Dados Empresa
    private Long idEmpresa;
    private String empresa;
    private String cnpj;
    private List<EmpresaDTO> empresas;

    
    //Dados Gerais
    private Date dtLancamento;
    private String loja;
    private String banco;
    private String agencia;
    private String contaCorrente;
    private String numDocumento;
    private String categoria;
    private String natureza;
    private String historico;
    private BigDecimal valorLancamento;
    private String tipo;
    private BigDecimal valorCofre;
    private String cofre;
    private String status;
    
    private Integer totalRegistros;
    
}

package br.com.accesstage.trustion.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaModeloNegocioDTO {

    @NotEmpty
    @NotBlank
    private Long idEmpresa;
    
    @NotEmpty
    @NotBlank
    private Long idModeloNegocio;
	
    private String modeloNegocio;

    private Long idUsuarioCriacao;
    private Long idUsuarioAlteracao;
	private Date dataCriacao;
	private Date dataAlteracao;

}

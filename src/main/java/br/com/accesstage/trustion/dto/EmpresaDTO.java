package br.com.accesstage.trustion.dto;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaDTO {

    private Long idEmpresa;

    @NotEmpty
    @NotBlank
    private String razaoSocial;

    @NotEmpty
    @NotBlank
    private String cnpj;

    @NotNull
    private Long idGrupoEconomico;
    private Long idEmpresaSegmento;
    private String endereco;

    private String nomeGrupoEconomico;
    private String cidade;
    private String estado;
    private String cep;

    private Date dataCriacao;
    private Long idUsuarioCriacao;
    private Date dataAlteracao;
    private Long idUsuarioAlteracao;

    private String siglaLoja;
    private String status;

    @NotNull
    List<DadosBancariosDTO> dadosBancarios;

    List<EmpresaModeloNegocioDTO> empresaModeloNegocios;
    		
}

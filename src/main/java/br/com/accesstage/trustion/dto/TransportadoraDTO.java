package br.com.accesstage.trustion.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"idUsuarioCriacao", "dataCriacao", "idUsuarioAlteracao", "dataAlteracao", "outrasEmpresas"})
public class TransportadoraDTO {

    private Long idTransportadora;

    @NotNull
    @NotEmpty
    @NotBlank
    private String razaoSocial;

    @NotNull
    @NotEmpty
    @NotBlank
    private String cnpj;

    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private String responsavel;
    private String nroTelefone;
    private String email;
    private String envioInformacao;
    private String status;
    private String statusDescricao;
    private Long idUsuarioCriacao;
    private Date dataCriacao;
    private Long idUsuarioAlteracao;
    private Date dataAlteracao;
    
    private List<GrupoEconomicoDTO> outrasEmpresas;
    
}

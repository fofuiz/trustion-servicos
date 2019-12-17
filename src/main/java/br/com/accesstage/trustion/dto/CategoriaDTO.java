package br.com.accesstage.trustion.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.accesstage.trustion.model.TipoCategoria;
import lombok.Data;

@Data
public class CategoriaDTO {

    private Integer idCategoria;
    @NotEmpty
    @NotBlank
    private String descricao;
    private Date dataCriacao;
    private Integer idUsuarioCriacao;
    private Date dataAlteracao;
    private Integer idUsuarioAlteracao;
    @NotEmpty
    @NotBlank
    private String status;
    private String statusDescricao;
    @NotNull
    private TipoCategoria idTipoCategoria;

}

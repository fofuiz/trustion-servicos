package br.com.accesstage.trustion.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoEmpresaTransportadoraUsuarioDTO {

    @NotNull
    private Long idGrupoEmpresa;

    @NotNull
    private Long transportadora;

    @NotNull
    private List<Long> empresas;

    @NotNull
    private List<Long> notificacoes;

}

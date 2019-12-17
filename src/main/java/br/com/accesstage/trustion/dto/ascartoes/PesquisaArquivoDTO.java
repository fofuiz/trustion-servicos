package br.com.accesstage.trustion.dto.ascartoes;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PesquisaArquivoDTO {

    private Long id;
    private String sequencial;
    private String nomeArquivo;
    private Date dataInicial;
    private Date dataFinal;
    private String dataEnvio;
    private Long qtdRegistro;
    @NotNull
    private String empId;
}

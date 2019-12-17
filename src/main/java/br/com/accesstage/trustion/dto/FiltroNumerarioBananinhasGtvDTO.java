package br.com.accesstage.trustion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroNumerarioBananinhasGtvDTO {

    @NotNull
    private int gtv;

}

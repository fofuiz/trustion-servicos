package br.com.accesstage.trustion.dto;

import lombok.*;

@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BkOfficeEntityDTO {

    private String bandeira;
    private double total;

}
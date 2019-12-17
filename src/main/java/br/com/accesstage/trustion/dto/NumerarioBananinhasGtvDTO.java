package br.com.accesstage.trustion.dto;

import br.com.accesstage.trustion.model.Pac;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class NumerarioBananinhasGtvDTO {

    //Fixo
    private int packDeclarada;
    private int packProcessada;
    private double vlrDeclaradoSoma; //Soma da coluna 'vlr_declarado'
    private double vlrConferidoSoma; //Soma da coluna 'vlr_conferido'
    private double diferencaSoma; //Diferença das somas das colunas acima

    //Corpo
    private BigInteger numeroPack;
    private double vlrDeclarado;
    private double vlrConferido;
    private double diferencaPorPack;
    private double cedulaFalsa;

    public NumerarioBananinhasGtvDTO() {

    }

    public NumerarioBananinhasGtvDTO(Pac pac) {
        //Fixo
        this.packDeclarada = pac.getPackDeclarada();
        this.packProcessada = pac.getPackProcessada();
        this.vlrDeclaradoSoma = pac.getVlrDeclaradoSoma();
        this.vlrConferidoSoma = pac.getVlrConferidoSoma();
        this.diferencaSoma = pac.getVlrDiferencaSoma();

        //Corpo
        this.numeroPack = pac.getNumeroPack();
        this.vlrDeclarado = pac.getVlrDeclarado();
        this.vlrConferido = pac.getVlrConferido();
        this.diferencaPorPack = pac.getDiferenca();
        this.cedulaFalsa = pac.getCedulaFalsa();
    }

    //Converter
    public static Page<NumerarioBananinhasGtvDTO> converter(Page<Pac> listaNumerarioBananinhas) {
        return listaNumerarioBananinhas.map(NumerarioBananinhasGtvDTO::new);
    }

    //Converter
    public static List<NumerarioBananinhasGtvDTO> converter(List<Pac> listaNumerarioBananinhas) {
        return listaNumerarioBananinhas.stream()
                .map( b -> new NumerarioBananinhasGtvDTO(b))
                .collect(Collectors.toList());
    }

}

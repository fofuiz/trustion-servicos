package br.com.accesstage.trustion.enums;

public enum LogsEnum {
    DOMBANC001("Domicilio bancário filtro banco api codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC002("Domicilio bancário filtro banco service codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC003("Domicilio bancário filtro banco service error codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC004("Domicilio bancário todos bancos filtro service codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC005("Domicilio bancário todos bancos service error codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC006("Domicilio bancário filtro banco api error codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC007("Domicilio bancário filtro banco repository codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC008("Domicilio bancário filtro banco repository error codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC009("Domicilio bancário filtro todos bancos repository codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC010("Domicilio bancário filtro todos bancos repository error codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC011("Domicilio bancário filtro banco api resultados codigoBanco={},numeroBanco={},empID={},totalResultados={}"),
    DOMBANC012("Domicilio bancário filtro banco api todos domicílio bancário codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC013("Domicilio bancário filtro banco api erro todos domicílio bancário codigoBanco={},numeroBanco={},empID={}"),
    DOMBANC014("Domicilio bancário filtro banco api resultados codigoBanco={},numeroBanco={},empID={},totalResultados={}"),

    ALUQEUIP001("Aluguies de Equipamentos api dataDe={},dataAte={},empId={}"),
    ALUQEUIP002("Aluguies de Equipamentos api error dataDe={},dataAte={},empId={}"),
    ALUQEUIP003("Aluguies de Equipamentos api resultados dataDe={},dataAte={},empId={},totalResultados={}"),
    ALUQEUIP004("Aluguies de Equipamentos service dataDe={},dataAte={},empId={}"),
    ALUQEUIP005("Aluguies de Equipamentos service dataDe={},dataAte={},empId={}, filiais={}"),
    ALUQEUIP007("Aluguies de Equipamentos repository dataDe={},dataAte={},empId={}"),
    ALUQEUIP008("Aluguies de Equipamentos repository error dataDe={},dataAte={},empId={}"),


    EMPCA001("Empresa filiais service empId={}"),
    ;

    private final String texto;

    LogsEnum(String texto){
        this.texto = texto;
    }

    public String texto() {
        return texto;
    }
}

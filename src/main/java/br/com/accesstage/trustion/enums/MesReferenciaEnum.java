package br.com.accesstage.trustion.enums;

public enum MesReferenciaEnum {

    MES_JANEIRO("01","Janeiro"),
    MES_FEVEREIRO("02","Fevereiro"),
    MES_MARCO("03","Mar\u00E7o"),
    MES_ABRIL("04","Abril"),
    MES_MAIO("05","Maio"),
    MES_JUNHO("06","Junho"),
    MES_JULHO("07","Julho"),
    MES_AGOSTO("08","Agosto"),
    MES_SETEMBRO("09","Setembro"),
    MES_OUTUBRO("10","Outubro"),
    MES_NOVEMBRO("11","Novembro"),
    MES_DEZEMBRO("12","Dezembro");

    private String codigoMes;
    private String labelMes;

    MesReferenciaEnum(String codigoMes, String labelMes){
        this.codigoMes = codigoMes;
        this.labelMes  = labelMes;
    }


    public String getCodigoMes() {
        return codigoMes;
    }
    public void setCodigoMes(String codigoMes) {
        this.codigoMes = codigoMes;
    }
    public String getLabelMes() {
        return labelMes;
    }
    public void setLabelMes(String labelMes) {
        this.labelMes = labelMes;
    }
}
package br.com.accesstage.trustion.dto;

public class FiltroTelasConciliacaoCartaoResumoDTO {

    private String numeroBanco;

    private String anoMesRefStr;

    private String anoRef;

    private String dataInicial;

    private String dataFinal;

    private String numeroAgencia;

    private String numeroConta;

    private String codigoOperadora;

    private Integer empId;

    private String statusConciliado;

    public String getNumeroBanco() {
        return numeroBanco;
    }

    public void setNumeroBanco(String numeroBanco) {
        this.numeroBanco = numeroBanco;
    }

    public String getAnoMesRefStr() {
        return anoMesRefStr;
    }

    public void setAnoMesRefStr(String anoMesRefStr) {
        this.anoMesRefStr = anoMesRefStr;
    }

    public String getAnoRef() {
        return anoRef;
    }

    public void setAnoRef(String anoRef) {
        this.anoRef = anoRef;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getNumeroAgencia() {
        return numeroAgencia;
    }

    public void setNumeroAgencia(String numeroAgencia) {
        this.numeroAgencia = numeroAgencia;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getCodigoOperadora() {
        return codigoOperadora;
    }

    public void setCodigoOperadora(String codigoOperadora) {
        this.codigoOperadora = codigoOperadora;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getStatusConciliado() {
        return statusConciliado;
    }

    public void setStatusConciliado(String statusConciliado) {
        this.statusConciliado = statusConciliado;
    }

    @Override
    public String toString() {
        return "FiltroTelasConciliacaoCartaoResumoDTO{" +
                "numeroBanco='" + numeroBanco + '\'' +
                ", anoMesRefStr='" + anoMesRefStr + '\'' +
                ", anoRef='" + anoRef + '\'' +
                ", dataInicial='" + dataInicial + '\'' +
                ", dataFinal='" + dataFinal + '\'' +
                ", numeroAgencia='" + numeroAgencia + '\'' +
                ", numeroConta='" + numeroConta + '\'' +
                ", codigoOperadora='" + codigoOperadora + '\'' +
                ", empId=" + empId +
                ", statusConciliado='" + statusConciliado + '\'' +
                '}';
    }
}

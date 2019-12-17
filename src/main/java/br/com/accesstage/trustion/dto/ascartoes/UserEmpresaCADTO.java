package br.com.accesstage.trustion.dto.ascartoes;

public class UserEmpresaCADTO {
    private Long idEmpresaCa;
    private String cnpj;
    private String razaoSocial;
    private int tpoRemessaEmpresa;
    private int idSegmento;


    public int getIdSegmento() {
        return idSegmento;
    }

    public void setIdSegmento(int idSegmento) {
        this.idSegmento = idSegmento;
    }

    public int getTpoRemessaEmpresa() {
        return tpoRemessaEmpresa;
    }

    public void setTpoRemessaEmpresa(int tpoRemessaEmpresa) {
        this.tpoRemessaEmpresa = tpoRemessaEmpresa;
    }

    public Long getIdEmpresaCa() {
        return idEmpresaCa;
    }

    public void setIdEmpresaCa(Long idEmpresaCa) {
        this.idEmpresaCa = idEmpresaCa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
}

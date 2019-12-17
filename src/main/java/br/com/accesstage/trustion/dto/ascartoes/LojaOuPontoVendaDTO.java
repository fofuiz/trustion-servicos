package br.com.accesstage.trustion.dto.ascartoes;

public class LojaOuPontoVendaDTO {

    private Long id;
    private Long codigo;
    private String nome;

    public LojaOuPontoVendaDTO(Long codigo, String nome) {
        super();
        this.id = codigo;
        this.codigo = codigo;
        this.nome = nome;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}

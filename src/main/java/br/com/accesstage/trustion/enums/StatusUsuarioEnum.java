package br.com.accesstage.trustion.enums;

public enum StatusUsuarioEnum {

	ATIVO("A"), 
	INATIVO("I"),
	SUSPENSO("S");
	
	private String status;
	
	private StatusUsuarioEnum(String status) {
		this.status = status;
	}
	
    public String get() {
        return status;
    }
}

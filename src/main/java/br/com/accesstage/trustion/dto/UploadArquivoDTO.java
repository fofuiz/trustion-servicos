package br.com.accesstage.trustion.dto;

public class UploadArquivoDTO {

	private Long idUploadArquivo;
	private Long idAtividade;
	private String nomeArquivo;
	private byte[] arquivo;

	public Long getIdUploadArquivo() {
		return idUploadArquivo;
	}
	
	public void setIdUploadArquivo(Long idUploadArquivo) {
		this.idUploadArquivo = idUploadArquivo;
	}
	
	public Long getIdAtividade() {
		return idAtividade;
	}
	
	public void setIdAtividade(Long idAtividade) {
		this.idAtividade = idAtividade;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public byte[] getArquivo() {
		return arquivo;
	}
	
	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}
}

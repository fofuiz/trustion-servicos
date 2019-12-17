package br.com.accesstage.trustion.enums;

public enum CodigoTipoStatusOcorrencia {
	ANALISE_SOLICITADA(1L), ANALISE_ANDAMENTO(2L), AGUARDANDO_APROVACAO(3L), CONCLUIDA(4L);
	
	private Long id;

	private CodigoTipoStatusOcorrencia(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}

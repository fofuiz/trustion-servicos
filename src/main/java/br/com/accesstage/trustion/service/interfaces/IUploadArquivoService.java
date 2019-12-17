package br.com.accesstage.trustion.service.interfaces;

import br.com.accesstage.trustion.dto.UploadArquivoDTO;

public interface IUploadArquivoService {

	void criar(UploadArquivoDTO uploadArquivoDTO) throws Exception;
	UploadArquivoDTO buscarPorAtividade(Long idAtividade) throws Exception;
}

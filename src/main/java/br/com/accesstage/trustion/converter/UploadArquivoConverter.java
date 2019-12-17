package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.UploadArquivoDTO;
import br.com.accesstage.trustion.model.UploadArquivo;

public class UploadArquivoConverter {

	public static UploadArquivo paraEntidade(UploadArquivoDTO uploadArquivoDTO) {

		UploadArquivo uploadArquivo = new UploadArquivo();

		uploadArquivo.setIdUploadArquivo(uploadArquivoDTO.getIdUploadArquivo());
		uploadArquivo.setIdAtividade(uploadArquivoDTO.getIdAtividade());
		uploadArquivo.setNomeArquivo(uploadArquivoDTO.getNomeArquivo());
		uploadArquivo.setArquivo(uploadArquivoDTO.getArquivo());

		return uploadArquivo;
	}

	public static UploadArquivoDTO paraDTO(UploadArquivo uploadArquivo) {

		UploadArquivoDTO uploadArquivoDTO = new UploadArquivoDTO();

		uploadArquivoDTO.setIdUploadArquivo(uploadArquivo.getIdUploadArquivo());
		uploadArquivoDTO.setIdAtividade(uploadArquivo.getIdAtividade());
		uploadArquivoDTO.setNomeArquivo(uploadArquivo.getNomeArquivo());
		uploadArquivoDTO.setArquivo(uploadArquivo.getArquivo());

		return uploadArquivoDTO;
	}
}

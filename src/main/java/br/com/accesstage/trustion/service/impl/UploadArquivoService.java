package br.com.accesstage.trustion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.converter.UploadArquivoConverter;
import br.com.accesstage.trustion.dto.UploadArquivoDTO;
import br.com.accesstage.trustion.model.UploadArquivo;
import br.com.accesstage.trustion.repository.interfaces.IUploadArquivoRepository;
import br.com.accesstage.trustion.service.interfaces.IUploadArquivoService;

@Service
public class UploadArquivoService implements IUploadArquivoService {

	@Autowired
	private IUploadArquivoRepository uploadArquivoRepository;

	@Override
	public void criar(UploadArquivoDTO uploadArquivoDTO) throws Exception {

		UploadArquivo uploadArquivo;

		uploadArquivo = UploadArquivoConverter.paraEntidade(uploadArquivoDTO);
		uploadArquivoRepository.save(uploadArquivo);
	}

	@Override
	public UploadArquivoDTO buscarPorAtividade(Long idAtividade) throws Exception {

		UploadArquivo uploadArquivo;
		UploadArquivoDTO uploadArquivoDTO;

		uploadArquivo = uploadArquivoRepository.findOneByIdAtividade(idAtividade);

		uploadArquivoDTO = UploadArquivoConverter.paraDTO(uploadArquivo);

		return uploadArquivoDTO;
	}
}

package br.com.accesstage.trustion.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.service.interfaces.IAtividadeService;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.accesstage.trustion.dto.UploadArquivoDTO;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IUploadArquivoService;
import br.com.accesstage.trustion.util.Mensagem;

@RestController
public class UploadEvidenciaController {

	@Autowired
	private IUploadArquivoService uploadArquivoService;

	@Autowired
	private IAtividadeService atividadeService;

	@Log
	private Logger LOG;

	@RequestMapping(value="/upload/atividade/{idAtividade}", method=RequestMethod.POST)
	public @ResponseBody void upload(@RequestParam("file") MultipartFile file, @PathVariable Long idAtividade) {

		LOG.info(Utils.getInicioMetodo());

		String fileName = "undefined";

		try {
			fileName = file.getOriginalFilename();

			if(!file.getContentType().contains("pdf")){
				atividadeService.excluir(idAtividade);
				throw new BadRequestResponseException(Mensagem.get("msg.erro.arquivo.extensao", new Object[]{fileName}));
			}

			UploadArquivoDTO uploadArquivoDTO = new UploadArquivoDTO();

			uploadArquivoDTO.setIdAtividade(idAtividade);
			uploadArquivoDTO.setNomeArquivo(fileName);
			uploadArquivoDTO.setArquivo(file.getBytes());

			uploadArquivoService.criar(uploadArquivoDTO);

		}catch (BadRequestResponseException e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.erro.fazer.upload.arquivo", new Object[]{fileName}));
		}

		LOG.info(Utils.getFimMetodo());
	}

	@RequestMapping(value="/download/atividade1/{idAtividade}", method=RequestMethod.GET, produces="application/pdf")
	public @ResponseBody void download1(@PathVariable("idAtividade") Long idAtividade, HttpServletResponse response) throws IOException {

		LOG.info(Utils.getInicioMetodo());

		try {
			UploadArquivoDTO uploadArquivoDTO = uploadArquivoService.buscarPorAtividade(idAtividade);

			InputStream inputStream = new ByteArrayInputStream(uploadArquivoDTO.getArquivo());

			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=" + uploadArquivoDTO.getNomeArquivo());
			response.setHeader("Content-Length", String.valueOf(uploadArquivoDTO.getArquivo().length));

			FileCopyUtils.copy(inputStream, response.getOutputStream());

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.efetuar.download.arquivo"));
		}

		LOG.info(Utils.getFimMetodo());
	}

	@RequestMapping(value="/download/atividade2/{idAtividade}", method=RequestMethod.GET, produces="application/pdf")
	public @ResponseBody HttpEntity<byte[]> download2(@PathVariable("idAtividade") Long idAtividade) throws IOException {

		LOG.info(Utils.getFimMetodo());

		try {
			UploadArquivoDTO uploadArquivoDTO = uploadArquivoService.buscarPorAtividade(idAtividade);

			HttpHeaders header = new HttpHeaders();
		    header.setContentType(new MediaType("application", "pdf"));
		    header.set("Content-Disposition", "inline; filename=" + uploadArquivoDTO.getNomeArquivo());
		    header.setContentLength(uploadArquivoDTO.getArquivo().length);

			LOG.info(Utils.getFimMetodo());
		    return new HttpEntity<byte[]>(uploadArquivoDTO.getArquivo(), header);

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.efetuar.download.arquivo"));
		}
	}
}

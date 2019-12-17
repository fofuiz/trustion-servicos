package br.com.accesstage.trustion.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import br.com.accesstage.trustion.dto.AtividadeDTO;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IAtividadeService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.RelatorioHtmlTamplate;

@RestController
public class AtividadeController {

	@Autowired
	private IAtividadeService atividadeService;

	@Log
	private Logger LOG;
	
	@RequestMapping(value = "/atividade", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AtividadeDTO> criar(@RequestBody AtividadeDTO dados){
		LOG.info(Utils.getInicioMetodo());

		AtividadeDTO dto = null;

		try {
			dto = atividadeService.criar(dados);

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"atividade"}));
		}

		LOG.info(Utils.getFimMetodo());
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/atividade/acao", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AtividadeDTO> criarPorAcao(@RequestBody AtividadeDTO dados){
		LOG.info(Utils.getInicioMetodo());

		AtividadeDTO dto;

		try {
			dto = atividadeService.criarPorAcao(dados);

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.criar", new Object[]{"atividade"}));
		}

		LOG.info(Utils.getFimMetodo());
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/atividade/aprovar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AtividadeDTO> aprovarOcorrencia(@RequestBody AtividadeDTO dados){
		LOG.info(Utils.getInicioMetodo());

		AtividadeDTO dto = null;

		try {
			dto = atividadeService.aprovar(dados);

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.aprovar", new Object[]{"atividade"}));
		}

		LOG.info(Utils.getFimMetodo());
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/atividade/rejeitar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AtividadeDTO> reprovarOcorrencia(@RequestBody AtividadeDTO dados){
		LOG.info(Utils.getInicioMetodo());

		AtividadeDTO dto = null;

		try {
			dto = atividadeService.rejeitar(dados);

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.rejeitar", new Object[]{"atividade"}));
		}

		LOG.info(Utils.getFimMetodo());
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@RequestMapping(value = "/atividades/ocorrencia/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<AtividadeDTO>> pesquisarPorOcorrencia(@PathVariable("id") Long idOcorrencia){
		LOG.info(Utils.getInicioMetodo());

		List<AtividadeDTO> listaDto = null;

		try {
			listaDto = atividadeService.listarPorOcorrencia(idOcorrencia);

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.pesquisar", new Object[]{"atividade"}));
		}

		LOG.info(Utils.getFimMetodo());
		return new ResponseEntity<>(listaDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/atividades/ocorrencia/{idOcorrencia}/arquivoResumoOcorrenciaPDF", method=RequestMethod.GET, produces="application/pdf")
	public @ResponseBody HttpEntity<byte[]> getArquivoResumoOcorrenciaPDF(@PathVariable("idOcorrencia") Long idOcorrencia) throws IOException {
		LOG.info(Utils.getInicioMetodo());

		try {
			byte[] arquivo = null;

			Document document = new Document(PageSize.LETTER);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("/home/lucas/lucas.pdf"));

			document.open();
			document.addAuthor("Accesstage");
			document.addCreator("Brinks-Portal");
			document.addSubject("Relatório");
			document.addTitle("Resumo da Ocorrência - " + idOcorrencia);
			document.addCreationDate();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			worker.parseXHtml(pdfWriter, document, 
					new StringReader(RelatorioHtmlTamplate.resumoOcorrencia(atividadeService.listarPorOcorrencia(idOcorrencia))));

			document.close();

			HttpHeaders header = new HttpHeaders();
		    header.setContentType(new MediaType("application", "pdf"));
		    header.set("Content-Disposition", "inline; filename=lucas.pdf");
		    //header.setContentLength(arquivo.length);

			LOG.info(Utils.getFimMetodo());

		    return new HttpEntity<byte[]>(arquivo, header);

		} catch (Exception e) {
			LOG.error("Exceção " +e.getMessage(), e);
			throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.efetuar.download.arquivo"));
		}
	}
}

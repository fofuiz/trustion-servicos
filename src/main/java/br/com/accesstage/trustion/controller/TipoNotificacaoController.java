package br.com.accesstage.trustion.controller;

import java.util.Collection;
import java.util.List;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.util.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.accesstage.trustion.model.TipoNotificacao;
import br.com.accesstage.trustion.repository.interfaces.ITipoNotificacaoRespository;

@RestController
public class TipoNotificacaoController {
		
		@Autowired
		private ITipoNotificacaoRespository tipoServicoRepository;

		@Log
		private Logger LOG;
	
		@RequestMapping(value="tipoNotificacao",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Collection<TipoNotificacao>> listarTodos(){
			LOG.info(Utils.getInicioMetodo());
			
			List<TipoNotificacao> lstTipoNotificacao = tipoServicoRepository.findAll();

			LOG.info(Utils.getFimMetodo());
			return new ResponseEntity<Collection<TipoNotificacao>>(lstTipoNotificacao,HttpStatus.OK);
		}
	
}

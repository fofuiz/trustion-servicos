package br.com.accesstage.trustion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.accesstage.hikercompcommons.vo.Integration;

import br.com.accesstage.trustion.carga.ETLPac;
import br.com.accesstage.trustion.carga.ETLVendas;
import br.com.accesstage.trustion.carga.ETLVideo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CargaController {

	@Autowired
	private ETLVideo service;

	@Autowired
	private ETLPac etlPac;


	@Autowired
	private ETLVendas etlVendas;

	@PostMapping("/cargavideo")
	public ResponseEntity<String> leArqVideo(@RequestBody Integration integration) throws Exception {
		try {
			service.etlVideo(integration);
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PostMapping("/cargapac")
	public ResponseEntity<String> realizaCargaPac(@RequestBody Integration integration) throws Exception {
		try {
			etlPac.etlPac(integration);
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}


	@PostMapping("/cargavendas")
	public ResponseEntity<String> realizaCargaVendas(@RequestBody Integration integration) throws Exception {
		try {
			etlVendas.etlCarga(integration);
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}

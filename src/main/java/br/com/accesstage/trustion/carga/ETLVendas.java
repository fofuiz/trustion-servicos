package br.com.accesstage.trustion.carga;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.accesstage.hikercompcommons.vo.Integration;

import br.com.accesstage.parsefile.ParseFile;
import br.com.accesstage.parsefile.retornos.Valores;
import br.com.accesstage.trustion.exception.RegistraErrorEventoException;
import br.com.accesstage.trustion.model.MovimentoDiario;
import br.com.accesstage.trustion.repository.interfaces.IVendasRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ETLVendas {

	@Value("${layout.carga.vendas.path}")
	private String path;

	@Value("${layout.carga.vendas.id}")
	private String id;

	@Autowired
	private IVendasRepository vendasRep;

	private Integration integration;

	/**
	 * metodo que e chamado pelo endpoint e orquestra as tarefas do ETL da carga de
	 * vendas.
	 * 
	 * @param integration obj Integration
	 * @throws Exception
	 */
	public void etlCarga(Integration integration) throws Exception {
		this.integration = integration;
		writeData(this.transformData(this.readData()));
	}

	/**
	 * metodo realiza a leitura do arquivo
	 * 
	 * @return String list
	 */
	private List<String> readData() {
		log.debug("readData: lendo arquivo");
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(integration.getDataFileName())));
			return br.lines().collect(Collectors.toList());
		} catch (Exception erro) {
			log.error("readFile: nao foi possivel ler o arquivo");
		}
		return null;
	}

	/**
	 * metodo recebe uma lista de strings lidas pelo readData instancia um obj
	 * ParseFile realiza o parse da string para um obj tipo Vendas retorna uma lista
	 * de obj do tipo vendas
	 * 
	 * @param strData string list, cada item da lista é uma linha do documento lido
	 * @return list obj Vendas
	 * @throws RegistraErrorEventoException
	 */
	private List<MovimentoDiario> transformData(List<String> strData) throws RegistraErrorEventoException {
		log.debug("transformData: processando dados");
		try {
			ParseFile pf = new ParseFile(path, id, true, true);
			List<MovimentoDiario> vendasData = new ArrayList<MovimentoDiario>();

			for (String string : strData) {
				try {
					Valores valores = pf.converteLinha("detalhe", string);
					MovimentoDiario vendas = (MovimentoDiario) pf.castObject(valores, MovimentoDiario.class);
					vendasData.add(vendas);
				} catch (Exception e) {
					log.info("Nao foi possivel parsear: " + string + e.getMessage());
				}
			}

			return vendasData;
		} catch (ParseException erro) {
			throw new RegistraErrorEventoException(erro.getMessage(), Long.parseLong(integration.getTrackingID()));
		} catch (Exception erro) {
			throw new RegistraErrorEventoException(erro.getMessage(), Long.parseLong(integration.getTrackingID()));
		}
	}

	/**
	 * metodo que recebe uma lista de objetos do tipo vendas e realiza a
	 * persistencia na base
	 * 
	 * @param vendasList list obj Vendas
	 */
	private void writeData(List<MovimentoDiario> vendasList) {
		log.debug("writeData: persistindo dados");
		try {
			vendasRep.save(vendasList);
		} catch (Exception erro) {
			log.error("writeData; erro tentar inserir os dados na base");
		}
	}

}

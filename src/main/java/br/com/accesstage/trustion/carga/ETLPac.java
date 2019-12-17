package br.com.accesstage.trustion.carga;

import br.com.accesstage.parsefile.ParseFile;
import br.com.accesstage.parsefile.retornos.Valores;
import br.com.accesstage.trustion.exception.RegistraErrorEventoException;
import br.com.accesstage.trustion.model.Pac;
import br.com.accesstage.trustion.repository.interfaces.IPacRepository;
import com.accesstage.hikercompcommons.vo.Integration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ETLPac {

    @Value("${layout.carga.pac.path}")
    private String path;

    @Value("${layout.carga.pac.id}")
    private String id;

    @Autowired
    private IPacRepository pacRep;

    private Integration integration;

    /**
     * metodo que e chamado pelo endpoint e orquestra as tarefas do ETL da carga de do arquivo pac.
     * @param integration obj Integration
     * @throws Exception
     */
    public void etlPac(Integration integration) throws Exception {
        this.integration = integration;
        writeData(this.transformData(this.readData()));
    }


    /**
     * metodo realiza a leitura do arquivo
     * @return String list
     */
    private List<String> readData() throws IOException {
        log.debug("readData: lendo arquivo");
        log.info("Entrou no read DATA");
        BufferedReader br  = new BufferedReader(new InputStreamReader(new FileInputStream(integration.getDataFileName())));
        try{
            return br.lines().collect(Collectors.toList());
        }catch (Exception e){
            log.error("readFile: nao foi possivel ler o arquivo");
        }finally {
            br.close();
        }
        return null;
    }


    /**
     * metodo recebe uma lista de strings lidas pelo readData
     * instancia um obj ParseFile
     * realiza o parse da string para um obj tiṕo Pac
     * retorna uma lista de obj do tipo Pac
     * @param strData string list, cada item da lista é uma linha do documento lido
     * @return list obj Video
     * @throws RegistraErrorEventoException
     */
    private List<Pac> transformData(List<String> strData) throws RegistraErrorEventoException {
        log.debug("transformData: processando dados");
        try {
            ParseFile pf = new ParseFile(path, id, true, true);

            List<Pac> videoData = new ArrayList<Pac>();

            for (int i = 0; i <= (strData.size() - 1); i++) {
                try {
                    Valores valores = pf.converteLinha("detalhe", strData.get(i));
                    Pac pac = (Pac) pf.castObject(valores, Pac.class);
                    videoData.add(pac);
                }catch (Exception e){
                    if (i > 0)
                        log.error("Nao foi possivel parsear: " + strData.get(i).toString() + e.getMessage());
                }
            }

            return videoData;

        } catch (ParseException e) {
            throw new RegistraErrorEventoException(e.getMessage(), Long.parseLong(integration.getTrackingID()));
        } catch (Exception e) {
            throw new RegistraErrorEventoException(e.getMessage(), Long.parseLong(integration.getTrackingID()));
        }
    }


    /**
     * metodo que recebe uma lista de objetos do tipo Pac e realiza a persistencia na base
     * @param videoList list obj Video
     */
    private void writeData(List<Pac> videoList){
        log.debug("writeData: persistindo dados");
        try{
            pacRep.save(videoList);
        }catch (Exception e ){
            log.error("writeData; erro tentar inserir os dados na base");
        }
    }
}

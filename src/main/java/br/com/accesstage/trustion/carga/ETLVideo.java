package br.com.accesstage.trustion.carga;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import br.com.accesstage.trustion.model.VideoGTV;
import br.com.accesstage.trustion.repository.interfaces.IVideoGTVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.accesstage.hikercompcommons.vo.Integration;

import br.com.accesstage.trustion.exception.RegistraErrorEventoException;
import br.com.accesstage.parsefile.ParseFile;
import br.com.accesstage.parsefile.retornos.Valores;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ETLVideo {

    @Value("${layout.path}")
    private String path;

    @Value("${layout.id}")
    private String id;

    @Autowired
    private IVideoGTVRepository videoRep;

    private Integration integration;

    /**
     * metodo que e chamado pelo endpoint e orquestra as tarefas do ETL da carga de video.
     * @param integration obj Integration
     * @throws Exception
     */
    public void etlVideo(Integration integration) throws Exception {
        this.integration = integration;
        writeData(this.transformData(this.readData()));
    }


    /**
     * metodo realiza a leitura do arquivo
     * @return String list
     */
    private List<String> readData() throws IOException {
        log.debug("readData: lendo arquivo");
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
     * realiza o parse da string para um obj tiṕo Video
     * retorna uma lista de obj do tipo video
     * @param strData string list, cada item da lista é uma linha do documento lido
     * @return list obj Video
     * @throws RegistraErrorEventoException
     */
    private List<VideoGTV> transformData(List<String> strData) throws RegistraErrorEventoException {
        log.debug("transformData: processando dados");
        try {

            ParseFile pf = new ParseFile(path, id, true, true);

            List<VideoGTV> videoData = new ArrayList<VideoGTV>();
            for (String string: strData) {
                try {
                    Valores valores = pf.converteLinha("detalhe", string);
                    VideoGTV videoGtv = (VideoGTV) pf.castObject(valores, VideoGTV.class);
                    videoData.add(videoGtv);
                }catch (Exception e){
                    log.info("Nao foi possivel parsear: " + string);
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
     * metodo que recebe uma lista de objetos do tipo video e realiza a persistencia na base
     * @param videoList list obj Video
     */
    private void writeData(List<VideoGTV> videoList){
        log.debug("writeData: persistindo dados");
        try{
            videoRep.save(videoList);
        }catch (Exception e ){
            log.error("writeData; erro tentar inserir os dados na base");
        }
    }

}



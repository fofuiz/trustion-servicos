package br.com.accesstage.trustion;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan("br.com.accesstage.trustion")
public class Configuracao {

    @Autowired
    public void configMapper(ObjectMapper objMapper) {
        objMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    @Bean
    public Gson getBuilder() {
        return new GsonBuilder().create();
    }

    public static void main(String[] args) {
        SpringApplication.run(Configuracao.class, args);
    }
}

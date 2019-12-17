package br.com.accesstage.trustion.controller;

import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoTotalDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.ForbiddenResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoCreditoD1Service;
import br.com.accesstage.trustion.service.interfaces.IRelatorioAnaliticoCreditoService;
import br.com.accesstage.trustion.util.Mensagem;
import br.com.accesstage.trustion.util.UTF8;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/relAnaliticoCredito")
public class RelatorioAnaliticoCreditoTotalController {

    private IRelatorioAnaliticoCreditoService relatorioAnaliticoCreditoService;

    private IRelatorioAnaliticoCreditoD1Service relatorioAnaliticoCreditoD1Service;

    @Log
    private Logger LOG;

    @Autowired
    public RelatorioAnaliticoCreditoTotalController(IRelatorioAnaliticoCreditoService relatorioAnaliticoCreditoService, IRelatorioAnaliticoCreditoD1Service relatorioAnaliticoCreditoD1Service) {
        this.relatorioAnaliticoCreditoService = relatorioAnaliticoCreditoService;
        this.relatorioAnaliticoCreditoD1Service = relatorioAnaliticoCreditoD1Service;
    }

    @GetMapping(value = "/total/dias/7", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RelatorioAnaliticoTotalDTO> calcularRelatorioTotal7Dias(){

        RelatorioAnaliticoTotalDTO dto = null;

        try{
            RelatorioAnaliticoTotalDTO dtod0 = relatorioAnaliticoCreditoService.calcularRelatorioTotal7Dias();
            RelatorioAnaliticoTotalDTO dtod1 = relatorioAnaliticoCreditoD1Service.calcularRelatorioTotal7Dias();
            BigDecimal total = new BigDecimal("0");
            total = total.add(dtod0.getTotal());
            total = total.add(dtod1.getTotal());
            dto = new RelatorioAnaliticoTotalDTO();
            dto.setSerie(new StringBuilder("Creditado"));
            dto.setTotal(total);
        }catch (BadRequestResponseException | ForbiddenResponseException e){
            LOG.error("Exceção: " + e.getMessage(), e);
            throw e;
        }catch (Exception e){
            LOG.error("Exceção " +e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Total dos " + UTF8.Relatorios}));
        }

        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/total/dias", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RelatorioAnaliticoTotalDTO> calcularRelatorioTotalPeriodoUsuario(){

        RelatorioAnaliticoTotalDTO dto = null;

        try{
            RelatorioAnaliticoTotalDTO dtod0 = relatorioAnaliticoCreditoService.calcularRelatorioTotalPeriodoUsuario();
            RelatorioAnaliticoTotalDTO dtod1 = relatorioAnaliticoCreditoD1Service.calcularRelatorioTotalPeriodoUsuario();
            BigDecimal total = new BigDecimal("0");
            total = total.add(dtod0.getTotal());
            total = total.add(dtod1.getTotal());
            dto = new RelatorioAnaliticoTotalDTO();
            dto.setSerie(new StringBuilder("Creditado"));
            dto.setTotal(total);
        }catch (BadRequestResponseException | ForbiddenResponseException e){
            LOG.error("Exceção: " + e.getMessage(), e);
            throw e;
        }catch (Exception e){
            LOG.error("Exceção " +e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get("msg.nao.foi.possivel.listar", new Object[]{"Total dos " + UTF8.Relatorios}));
        }

        return ResponseEntity.ok(dto);
    }
}

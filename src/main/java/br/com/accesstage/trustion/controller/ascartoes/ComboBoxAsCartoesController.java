package br.com.accesstage.trustion.controller.ascartoes;

import br.com.accesstage.trustion.ascartoes.model.OpcaoExtratoCA;
import br.com.accesstage.trustion.ascartoes.model.OperadoraCA;
import br.com.accesstage.trustion.ascartoes.model.ProdutoOperadoraCA;
import br.com.accesstage.trustion.configs.log.Log;
import br.com.accesstage.trustion.dto.ascartoes.FiltroEmpCnpjDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroEmpIdDTO;
import br.com.accesstage.trustion.dto.ascartoes.LojaOuPontoVendaDTO;
import br.com.accesstage.trustion.exception.BadRequestResponseException;
import br.com.accesstage.trustion.exception.InternalServerErrorResponseException;
import br.com.accesstage.trustion.service.ascartoes.interfaces.*;
import br.com.accesstage.trustion.service.impl.ascartoes.EmpresaCaService;
import br.com.accesstage.trustion.util.Mensagem;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class ComboBoxAsCartoesController {

    @Autowired
    private EmpresaCaService empresaCaService;

    @Autowired
    private ILojaCAService lojaCAService;

    @Autowired
    private IPontoVendaCAService pontoVendaCAService;

    @Autowired
    private IOperadoraCAService operadoraCAService;

    @Autowired
    private IProdutoOperadoraCAService produtoOperadoraCAService;

    @Autowired
    private IOpcaoExtratoCAService opcaoExtratoCAService;

    @Log
    private static Logger LOGGER;

    private static final String MSG = "msg.nao.foi.possivel.listar";

    /**
     * Metodo para carregar combo box de loja e pontos de venda para filtro para
     * tela gestao de vendas
     *
     * @param filtro
     * @param camposInvalidos
     * @return
     */
    @RequestMapping(value = "/carregarComboLoja", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<LojaOuPontoVendaDTO>> carregarComboLoja(@Valid @ModelAttribute FiltroEmpIdDTO filtro, BindingResult camposInvalidos) {

        try {

            if (camposInvalidos.hasErrors()) {
                throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
            }

            List<LojaOuPontoVendaDTO> dtos = lojaCAService.carregarComboLoja(filtro.getEmpId());

            return new ResponseEntity<>(dtos, HttpStatus.OK);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Erro carregarComboLoja: " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro carregarComboLoja: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"carregarComboLoja"}));
        }
    }

    /**
     * Metodo para carregar combo box de loja e pontos de venda para filtro para
     * tela gestao de vendas
     *
     * @param filtro
     * @param camposInvalidos
     * @return
     */
    @RequestMapping(value = "/carregarComboLojaPorCnpjEmpresa", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<LojaOuPontoVendaDTO>> carregarComboLojaPorCnpjEmpresa(@Valid @ModelAttribute FiltroEmpCnpjDTO filtro, BindingResult camposInvalidos) {

        try {

            if (camposInvalidos.hasErrors()) {
                throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
            }

            List<LojaOuPontoVendaDTO> dtos = lojaCAService.carregarComboLojaPorCnpjEmpresa(filtro.getCnpj());

            return new ResponseEntity<>(dtos, HttpStatus.OK);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Erro carregarComboLojaPorCnpjEmpresa: " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro carregarComboLojaPorCnpjEmpresa: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"carregarComboLojaPorCnpjEmpresa"}));
        }
    }

    /**
     * Metodo para carregar combo box de somente com as lojas filtro para
     * tela Taxa Admiministrativas
     *
     * @param filtro
     * @param camposInvalidos
     * @return
     */
    @RequestMapping(value = "/carregarComboSomenteLoja", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<LojaOuPontoVendaDTO>> carregarComboSomenteLoja(@Valid @ModelAttribute FiltroEmpIdDTO filtro, BindingResult camposInvalidos) {

        try {

            if (camposInvalidos.hasErrors()) {
                throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
            }

            List<LojaOuPontoVendaDTO> dtos = lojaCAService.carregarComboSomenteLoja(filtro.getEmpId());

            return new ResponseEntity<>(dtos, HttpStatus.OK);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Erro carregarComboSomenteLoja: " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro carregarComboSomenteLoja: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"carregarComboSomenteLoja"}));
        }
    }

    /**
     * Metodo para carregar combo box de loja somente com ponto de venda para
     * tela de Taxa Administrativas
     *
     * @param filtro
     * @param camposInvalidos
     * @return
     */
    @RequestMapping(value = "/carregarComboPontoVenda", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<LojaOuPontoVendaDTO>> carregarComboPontoVenda(@Valid @ModelAttribute FiltroEmpIdDTO filtro, BindingResult camposInvalidos) {

        try {

            if (camposInvalidos.hasErrors()) {
                throw new BadRequestResponseException(Mensagem.get(camposInvalidos));
            }

            List<LojaOuPontoVendaDTO> dtos = pontoVendaCAService.carregarComboPontoVenda(filtro.getEmpId());

            return new ResponseEntity<>(dtos, HttpStatus.OK);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Erro carregarComboPontoVenda: " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro carregarComboPontoVenda: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"carregarComboPontoVenda"}));
        }
    }


    @RequestMapping(value = "/carregarComboPontoVendaPorCodLoja", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<LojaOuPontoVendaDTO>> carregarComboPontoVendaPorCodLoja(@Param("codLoja") Long codLoja) {

        try {

            List<LojaOuPontoVendaDTO> dtos = pontoVendaCAService.carregarComboPontoVendaPorCodLoja(codLoja);

            return new ResponseEntity<>(dtos, HttpStatus.OK);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Erro carregarComboPontoVendaPorLoja: " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro carregarComboPontoVendaPorLoja: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"carregarComboPontoVendaPorLoja"}));
        }
    }

    /**
     * Metodo para carregar combo box de operadora para filtro para tela de
     * pesquisa
     *
     * @return
     */
    @RequestMapping(value = "/carregarComboOperadora", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OperadoraCA>> carregarComboOperadora() {

        try {

            List<OperadoraCA> dtos = operadoraCAService.carregarComboOperadora(true);

            return new ResponseEntity<>(dtos, HttpStatus.OK);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Erro carregarComboOperadora: " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro carregarComboOperadora: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"carregarComboOperadora"}));
        }
    }

    /**
     * Metodo para carregar combo box de produto para filtro para tela de
     * pesquisa
     *
     * @param codOperadora
     * @return
     */
    @RequestMapping(value = "/carregarComboProdutoOperadora", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ProdutoOperadoraCA>> carregarComboProdutoOperadora(@Param("codOperadora") Long codOperadora) {

        try {

            List<ProdutoOperadoraCA> dtos = new ArrayList<>();

            if (codOperadora != null) {
                dtos = produtoOperadoraCAService.carregarComboProdutoOperadora(codOperadora);
            }

            return new ResponseEntity<>(dtos, HttpStatus.OK);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Erro carregarComboProdutoOperadora: " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro carregarComboProdutoOperadora: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"carregarComboProdutoOperadora"}));
        }
    }

    /**
     * Método para carregar combo box de opção de extrato para filtro em tela de
     * pesquisa.
     *
     * @return
     */
    @RequestMapping(value = "/carregarComboOpcaoExtrato", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<OpcaoExtratoCA>> carregarComboOpcaoExtrato() {

        try {

            List<OpcaoExtratoCA> dtos = opcaoExtratoCAService.carregarComboOpcaoExtrato();

            return new ResponseEntity<>(dtos, HttpStatus.OK);

        } catch (BadRequestResponseException e) {
            LOGGER.error("Erro carregarComboOpcaoExtrato: " + e.getMessage(), e);
            throw new BadRequestResponseException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro carregarComboOpcaoExtrato: " + e.getMessage(), e);
            throw new InternalServerErrorResponseException(Mensagem.get(MSG, new Object[]{"carregarComboOpcaoExtrato"}));
        }
    }

}

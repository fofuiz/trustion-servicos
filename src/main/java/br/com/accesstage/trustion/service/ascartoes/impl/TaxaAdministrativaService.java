package br.com.accesstage.trustion.service.ascartoes.impl;

import br.com.accesstage.trustion.ascartoes.model.*;

import java.util.List;

import br.com.accesstage.trustion.repository.ascartoes.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxaAdministrativaDTO;
import br.com.accesstage.trustion.dto.ascartoes.FiltroTaxasAdministrativasCadastroDTO;
import br.com.accesstage.trustion.dto.ascartoes.LojaOuPontoVendaDTO;
import br.com.accesstage.trustion.dto.ascartoes.TaxaAdministrativaCadastroDTO;
import br.com.accesstage.trustion.enums.ascartoes.TaxaAdministrativaPlanoEnum;
import br.com.accesstage.trustion.repository.ascartoes.impl.TaxaAdministrativaRepository;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IOperadoraCAService;
import br.com.accesstage.trustion.service.ascartoes.interfaces.ILojaCAService;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IPontoVendaCAService;
import br.com.accesstage.trustion.service.ascartoes.interfaces.IProdutoOperadoraCAService;
import br.com.accesstage.trustion.service.ascartoes.interfaces.ITaxaAdministrativaService;
import br.com.accesstage.trustion.util.Funcoes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaxaAdministrativaService implements ITaxaAdministrativaService {

    @Autowired
    private TaxaAdministrativaRepository repository;

    @Autowired
    private ITaxaAdministrativaRepository iRepository;

    @Autowired
    private IProdutoCARepository produtoCARepository;

    @Autowired
    private IProdutoOperadoraCARepository produtoOperadoraCARepository;

    @Autowired
    private ILojaCARepository lojaCARepository;

    @Autowired
    private IOperadoraCARepository operadoraCARepository;

    @Autowired
    private ILojaCAService lojaCAService;

    @Autowired
    private IPontoVendaCAService pontoVendaCAService;

    @Autowired
    private IPontoVendaCARepository pontoVendaCARepository;
    
    @Autowired
    private IOperadoraCAService operadoraCAService;

    @Autowired
    private IProdutoOperadoraCAService produtoOperadoraCAService;

    @Override
    @Transactional(readOnly = true)
    public List<TaxaAdministrativaDTO> pesquisar(FiltroTaxaAdministrativaDTO filtro) throws DataAccessException {
        return repository.consulta(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaxaAdministrativaDTO> pesquisarPage(FiltroTaxaAdministrativaDTO dto, Pageable pageable) throws DataAccessException {
        return repository.consultaPage(dto, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaxaAdministrativaCadastroDTO> pesquisarCadastro(FiltroTaxasAdministrativasCadastroDTO filtro) throws DataAccessException {
        List<FiltroTaxasAdministrativasCadastroDTO> listaFiltros = new ArrayList<FiltroTaxasAdministrativasCadastroDTO>();
        List<TaxaAdministrativaCadastroDTO> listaResultado = new ArrayList<>();

        LojaCA lojaCA = lojaCARepository.findOne(filtro.getCodLoja());
        PontoVendaCA pontoVendaCA = pontoVendaCARepository.findOne(filtro.getCodPontoVenda());

        List<OperadoraCA> listOperadoraCA = new ArrayList<>();
        if(filtro.getCodOperadora() != null){
            OperadoraCA operadora = operadoraCARepository.findOne(filtro.getCodOperadora());
            listOperadoraCA.add(operadora);
        } else {
            listOperadoraCA = operadoraCAService.carregarComboOperadora(true);
        }

        for(OperadoraCA operadoraCA : listOperadoraCA){
//            List<ProdutoOperadoraCA> listaProdutos = produtoOperadoraCAService.carregarComboProdutoOperadora(operadoraCA.getId());
            List<ProdutoOperadoraCA> listaProdutos = new ArrayList<>();
            if(filtro.getCodProduto() != null){
                ProdutoCA prd = produtoCARepository.findOne(filtro.getCodProduto());
                List<ProdutoOperadoraCA> listaProdSelecionada = produtoOperadoraCAService.buscarProdutoPorOperadoraECodProduto(operadoraCA.getId(), prd.getId());
                listaProdutos.addAll(listaProdSelecionada);
            } else {
                listaProdutos = produtoOperadoraCAService.carregarComboProdutoOperadora(operadoraCA.getId());
            }

            for(ProdutoOperadoraCA produtoOperadoraCA : listaProdutos){
                ProdutoCA produtoCA = produtoOperadoraCA.getProduto();
                    filtro.setCodOperadora(operadoraCA.getId());
                    filtro.setCodProduto(produtoCA.getId());
                    List<TaxaAdministrativa> listataxas = repository.consultarCadastro(filtro);

                    if(listataxas.isEmpty()){
                        TaxaAdministrativaCadastroDTO taxaDTO = new TaxaAdministrativaCadastroDTO();
                        taxaDTO.setCodEmp(filtro.getEmpId());
                        taxaDTO.setCodLoja(filtro.getCodLoja());
                        taxaDTO.setCodOperadora(operadoraCA.getId());
                        taxaDTO.setCodProduto(produtoCA.getId());
                        taxaDTO.setCodPontoVenda(filtro.getCodPontoVenda());
                        taxaDTO.setNmePontoVenda(Funcoes.removeZerosEsquerda(pontoVendaCA.getNumeroTerminal().trim()));
                        taxaDTO.setNmeLoja(lojaCA.getNome());
                        taxaDTO.setNmeOperadora(operadoraCA.getNmeOperadora());
                        taxaDTO.setNmeProduto(produtoCA.getNome());
                        listaResultado.add(taxaDTO);
                    }
                filtro.setCodOperadora(null);
                filtro.setCodProduto(null);

                    for(TaxaAdministrativa taxa : listataxas){
                        TaxaAdministrativaCadastroDTO taxaDTO = new TaxaAdministrativaCadastroDTO();
                        taxaDTO.setCodLoja(filtro.getCodLoja());
                        taxaDTO.setCodOperadora(operadoraCA.getId());
                        taxaDTO.setCodProduto(produtoCA.getId());

                        if (listaResultado.contains(taxaDTO)) {
                            int indice = listaResultado.indexOf(taxaDTO);
                            taxaDTO = listaResultado.get(indice);
                            listaResultado.remove(indice);
                        }

                        if (taxa.getLoja() == null && taxa.getLoja().getNmePontoVenda() == null) {
                            taxaDTO.setNmePontoVenda(Funcoes.removeZerosEsquerda(pontoVendaCA.getNmePontoVenda().trim()));
                        } else {
                            taxaDTO.setNmePontoVenda(Funcoes.removeZerosEsquerda(taxa.getLoja().getNmePontoVenda().trim()));
                        }

                        if(taxa.getLoja() != null && taxa.getNmeLoja() != null) {
                            taxaDTO.setNmeLoja(taxa.getNmeLoja());
                        } else {
                            taxaDTO.setNmeLoja(lojaCA.getNome());
                        }
                        taxaDTO.setNmeOperadora(operadoraCA.getNmeOperadora());
                        taxaDTO.setNmeProduto(produtoCA.getNome());

                        if(taxa.getCodUsuario() != null) {
                            taxaDTO.setCodUsuario(taxa.getCodUsuario());
                        }
                        if(taxa.getCodTaxaAdministrativa() != null){
                            taxaDTO.setCodTaxaAdministrativa(taxa.getCodTaxaAdministrativa());
                        }

                        int plano = taxa.getNroPlano().intValue();
                        switch (plano) {
                            case 1:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null){
                                    taxaDTO.setNroPlano1(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_1, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            case 2:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null) {
                                    taxaDTO.setNroPlano2(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_2, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            case 3:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null) {
                                    taxaDTO.setNroPlano3(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_3, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            case 4:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null) {
                                    taxaDTO.setNroPlano4(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_4, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            case 5:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null) {
                                    taxaDTO.setNroPlano5(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_5, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            case 6:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null) {
                                    taxaDTO.setNroPlano6(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_6, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            case 7:
                                taxaDTO.setNroPlano7(taxa.getTxAdmCadastrada());
                                taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_7, taxa.getCodTaxaAdministrativa());
                                break;
                            case 8:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null) {
                                    taxaDTO.setNroPlano8(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_8, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            case 9:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null) {
                                    taxaDTO.setNroPlano9(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_9, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            case 10:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null) {
                                    taxaDTO.setNroPlano10(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_10, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            case 11:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null) {
                                    taxaDTO.setNroPlano11(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_11, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            case 12:
                                if(taxa.getCodTaxaAdministrativa() != null && taxa.getTxAdmCadastrada() != null) {
                                    taxaDTO.setNroPlano12(taxa.getTxAdmCadastrada());
                                    taxaDTO.getMapaId().put(TaxaAdministrativaPlanoEnum.PLANO_12, taxa.getCodTaxaAdministrativa());
                                }
                                break;
                            default:
                                break;
                        }
                        listaResultado.add(taxaDTO);
                    }
                 }
            }
        return listaResultado;
    }

    /**
     * Metodo para salvar ou atualizar taxa administrativa
     *
     * @param listTaxaAdminitrativaCadastro
     * @return
     * @throws DataAccessException
     */
    @Override
    @Transactional
    public List<TaxaAdministrativa> salvar(List<TaxaAdministrativaCadastroDTO> listTaxaAdminitrativaCadastro) throws DataAccessException {
        BigDecimal valor;
        List<TaxaAdministrativa> retList = new ArrayList();
        for (TaxaAdministrativaCadastroDTO taxaDTO : listTaxaAdminitrativaCadastro) {
            for (TaxaAdministrativaPlanoEnum plano : TaxaAdministrativaPlanoEnum.values()) {
                if (taxaDTO.getListaSalvar().contains(plano)) {
                    TaxaAdministrativa taxa = new TaxaAdministrativa();
                    taxa.setCodEmp(taxaDTO.getCodEmp());
                    taxa.setCodOperadora(taxaDTO.getCodOperadora());
                    taxa.setCodPontoVenda(taxaDTO.getCodPontoVenda());
                    taxa.setCodProduto(taxaDTO.getCodProduto());
                    taxa.setCodTaxaAdministrativa(taxaDTO.getMapaId().get(plano));
                    taxa.setCodUsuario(taxaDTO.getCodUsuario());
                    taxa.setDtaAlteracao(Calendar.getInstance().getTime());
                    taxa.setNroPlano(plano.getNumeroPlano());
                    valor = null;
                    switch (plano) {
                        case PLANO_1:
                            valor = taxaDTO.getNroPlano1();
                            break;
                        case PLANO_2:
                            valor = taxaDTO.getNroPlano2();
                            break;
                        case PLANO_3:
                            valor = taxaDTO.getNroPlano3();
                            break;
                        case PLANO_4:
                            valor = taxaDTO.getNroPlano4();
                            break;
                        case PLANO_5:
                            valor = taxaDTO.getNroPlano5();
                            break;
                        case PLANO_6:
                            valor = taxaDTO.getNroPlano6();
                            break;
                        case PLANO_7:
                            valor = taxaDTO.getNroPlano7();
                            break;
                        case PLANO_8:
                            valor = taxaDTO.getNroPlano8();
                            break;
                        case PLANO_9:
                            valor = taxaDTO.getNroPlano9();
                            break;
                        case PLANO_10:
                            valor = taxaDTO.getNroPlano10();
                            break;
                        case PLANO_11:
                            valor = taxaDTO.getNroPlano11();
                            break;
                        case PLANO_12:
                            valor = taxaDTO.getNroPlano12();
                            break;
                        default:
                            break;
                    }
                    taxa.setTxAdmCadastrada(valor);
                    retList.add(iRepository.save(taxa));
                }
            }
            taxaDTO.getListaSalvar().clear();
        }
        return retList;
    }
}
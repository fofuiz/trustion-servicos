package br.com.accesstage.trustion.service.impl;

import br.com.accesstage.trustion.converter.DetalheConferenciaConverter;
import br.com.accesstage.trustion.dto.DetalheConferenciaDTO;
import br.com.accesstage.trustion.dto.RelatorioAnaliticoCreditoD1DTO;
import br.com.accesstage.trustion.enums.CodigoTipoCreditoEnum;
import br.com.accesstage.trustion.model.DetalheConferencia;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.ModeloNegocio;
import br.com.accesstage.trustion.model.TipoCredito;
import br.com.accesstage.trustion.model.Transportadora;
import br.com.accesstage.trustion.repository.interfaces.IDetalheConferenciaRepository;
import br.com.accesstage.trustion.repository.interfaces.IEmpresaRepository;
import br.com.accesstage.trustion.repository.interfaces.IModeloNegocioRepository;
import br.com.accesstage.trustion.repository.interfaces.ITipoCreditoRepository;
import br.com.accesstage.trustion.repository.interfaces.ITransportadoraRepository;
import br.com.accesstage.trustion.service.interfaces.IDetalheConferenciaService;
import br.com.accesstage.trustion.service.interfaces.IModeloNegocioService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalheConferenciaService implements IDetalheConferenciaService {

    @Autowired
    private IDetalheConferenciaRepository detalheConferenciaRepository;

    @Autowired
    private IEmpresaRepository empresaRepository;

    @Autowired
    private IModeloNegocioService modeloNegocioService;

    @Autowired
    private IModeloNegocioRepository modeloRepository;

    @Autowired
    private ITransportadoraRepository transportadoraRepository;

    @Autowired
    private ITipoCreditoRepository tipoCreditoRepository;

    @Override
    public List<DetalheConferenciaDTO> pesquisarDetalheConferencia(RelatorioAnaliticoCreditoD1DTO relatorioAnaliticoCreditoD1DTO) throws Exception {

        List<DetalheConferenciaDTO> detalheConferenciaDTOList = new ArrayList<>();
        List<DetalheConferencia> detalheConferenciaList = detalheConferenciaRepository.findByNumeroGVT(relatorioAnaliticoCreditoD1DTO.getGtv().trim());

        if (!detalheConferenciaList.isEmpty()) {
            DetalheConferencia detalheConferencia = detalheConferenciaList.get(0);

            TipoCredito tipoCredito = tipoCreditoRepository.findOne(CodigoTipoCreditoEnum.CREDITOD1.getId());
            List<Long> idsModelos = modeloNegocioService.listarIdsModeloNegocioPorTipoCredito(tipoCredito);

            DetalheConferenciaDTO detalheConferenciaDTO = DetalheConferenciaConverter.paraDTO(detalheConferencia);

            if (detalheConferencia.getCnpjCliente() != null) {
                Empresa empresa = empresaRepository.findOneByCnpjAndEmpresaModeloNegocioList_ModeloNegocio_IdModeloNegocioIn(detalheConferencia.getCnpjCliente(), idsModelos);
                if (empresa != null) {
                    detalheConferenciaDTO.setEmpresa(empresa.getRazaoSocial());
                }
            }

            if (relatorioAnaliticoCreditoD1DTO.getIdTransportadora() != null) {
                Transportadora transportadora = transportadoraRepository.findOne(relatorioAnaliticoCreditoD1DTO.getIdTransportadora());
                if (transportadora != null) {
                    detalheConferenciaDTO.setTransportadora(transportadora.getRazaoSocial());
                }
            }

            if (relatorioAnaliticoCreditoD1DTO.getIdModeloNegocio() != null) {
                ModeloNegocio modeloNegocio = modeloRepository.findOne(relatorioAnaliticoCreditoD1DTO.getIdModeloNegocio());
                if (modeloNegocio != null) {
                    detalheConferenciaDTO.setModeloNegocio(modeloNegocio.getNome());
                }
            }

            if (detalheConferencia.getValorConferido() != null) {
                if (detalheConferencia.getValorDeclarado() != null) {
                    detalheConferenciaDTO.setDiferenca(detalheConferencia.getValorConferido().subtract(detalheConferencia.getValorDeclarado()));
                } else {
                    detalheConferenciaDTO.setDiferenca(detalheConferencia.getValorConferido());
                }
            } else {
                detalheConferenciaDTO.setDiferenca(detalheConferencia.getValorDeclarado() == null ? null : detalheConferencia.getValorDeclarado().negate());
            }

            detalheConferenciaDTOList.add(detalheConferenciaDTO);
        }

        return detalheConferenciaDTOList;
    }

}

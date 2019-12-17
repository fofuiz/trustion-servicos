package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.EmpresaDTO;
import br.com.accesstage.trustion.dto.EmpresaModeloNegocioDTO;
import br.com.accesstage.trustion.model.Empresa;
import br.com.accesstage.trustion.model.EmpresaModeloNegocio;
import br.com.accesstage.trustion.model.ModeloNegocio;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmpresaConverter {

    public static Empresa paraEntidade(EmpresaDTO dto) {

        Empresa empresa = new Empresa();

        empresa.setIdEmpresa(dto.getIdEmpresa());
        empresa.setRazaoSocial(dto.getRazaoSocial());
        empresa.setCnpj(dto.getCnpj());
        empresa.setIdGrupoEconomico(dto.getIdGrupoEconomico());
        empresa.setIdEmpresaSegmento(dto.getIdEmpresaSegmento());
        empresa.setEndereco(dto.getEndereco());
        empresa.setCidade(dto.getCidade());
        empresa.setEstado(dto.getEstado());
        empresa.setCep(dto.getCep());
        empresa.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        empresa.setDataCriacao(dto.getDataCriacao());
        empresa.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
        empresa.setDataAlteracao(dto.getDataAlteracao());
        empresa.setStatus(dto.getStatus());
        empresa.setSiglaLoja(dto.getSiglaLoja());

        if (dto.getEmpresaModeloNegocios() != null && !dto.getEmpresaModeloNegocios().isEmpty()) {

            List<EmpresaModeloNegocio> empresaModeloNegocioList = new ArrayList<>();
            dto.getEmpresaModeloNegocios().forEach(emn -> {
                EmpresaModeloNegocio empresaModeloNegocio = new EmpresaModeloNegocio();
                empresaModeloNegocio.setEmpresa(empresa);
                ModeloNegocio modelo = new ModeloNegocio();
                modelo.setIdModeloNegocio(emn.getIdModeloNegocio());
                empresaModeloNegocio.setModeloNegocio(modelo);
                empresaModeloNegocioList.add(empresaModeloNegocio);
            });
            empresa.setEmpresaModeloNegocioList(empresaModeloNegocioList);
        }

        return empresa;
    }

    public static EmpresaDTO paraDTO(Empresa empresa) {

        EmpresaDTO dto = new EmpresaDTO();
        dto.setIdEmpresa(empresa.getIdEmpresa());
        dto.setRazaoSocial(empresa.getRazaoSocial());
        dto.setCnpj(empresa.getCnpj());
        dto.setIdGrupoEconomico(empresa.getIdGrupoEconomico());
        dto.setIdEmpresaSegmento(empresa.getIdEmpresaSegmento());
        dto.setEndereco(empresa.getEndereco());
        dto.setCidade(empresa.getCidade());
        dto.setEstado(empresa.getEstado());
        dto.setCep(empresa.getCep());
        dto.setIdUsuarioCriacao(empresa.getIdUsuarioCriacao());
        dto.setIdUsuarioAlteracao(empresa.getIdUsuarioCriacao());
        dto.setDataCriacao(empresa.getDataCriacao());
        dto.setDataAlteracao(empresa.getDataAlteracao());
        dto.setSiglaLoja(empresa.getSiglaLoja());
        dto.setStatus(empresa.getStatus());

        if (empresa.getEmpresaModeloNegocioList() != null) {
            dto.setEmpresaModeloNegocios(
                    empresa.getEmpresaModeloNegocioList()
                            .stream()
                            .map(e -> EmpresaModeloNegocioDTO
                                    .builder()
                                    .idEmpresa(e.getEmpresa().getIdEmpresa())
                                    .idModeloNegocio(e.getModeloNegocio().getIdModeloNegocio())
                                    .build()).collect(Collectors.toList()));
        }

        return dto;
    }
}

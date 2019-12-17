package br.com.accesstage.trustion.converter;

import br.com.accesstage.trustion.dto.TransportadoraDTO;
import br.com.accesstage.trustion.enums.StatusAtivoInativo;
import br.com.accesstage.trustion.enums.StatusUsuarioEnum;
import br.com.accesstage.trustion.model.Transportadora;

public class TransportadoraConverter {

    public static Transportadora paraEntidade(TransportadoraDTO dto) {

        Transportadora t = new Transportadora();

        t.setCep(dto.getCep());
        t.setCidade(dto.getCidade());
        t.setCnpj(dto.getCnpj());
        t.setEndereco(dto.getEndereco());
        t.setEnvioInformacao(dto.getEnvioInformacao());
        t.setEstado(dto.getEstado());
        t.setNroTelefone(dto.getNroTelefone());
        t.setEmail(dto.getEmail());
        t.setStatus(dto.getStatus());
        t.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        t.setDataCriacao(dto.getDataCriacao());
        t.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
        t.setDataAlteracao(dto.getDataAlteracao());
        t.setRazaoSocial(dto.getRazaoSocial());
        t.setResponsavel(dto.getResponsavel());
        t.setIdTransportadora(dto.getIdTransportadora());

        return t;
    }

    public static TransportadoraDTO paraDTO(Transportadora t) {

        TransportadoraDTO dto = new TransportadoraDTO();

        dto.setIdTransportadora(t.getIdTransportadora());
        dto.setCep(t.getCep());
        dto.setCidade(t.getCidade());
        dto.setCnpj(t.getCnpj());
        dto.setEndereco(t.getEndereco());
        dto.setEnvioInformacao(t.getEnvioInformacao());
        dto.setEstado(t.getEstado());
        dto.setNroTelefone(t.getNroTelefone());
        dto.setEmail(t.getEmail());
        dto.setStatus(t.getStatus());
        dto.setDataCriacao(t.getDataCriacao());
        dto.setIdUsuarioCriacao(t.getIdUsuarioCriacao());
        dto.setDataAlteracao(t.getDataAlteracao());
        dto.setIdUsuarioAlteracao(t.getIdUsuarioAlteracao());
        dto.setRazaoSocial(t.getRazaoSocial());
        dto.setResponsavel(t.getResponsavel());

        if (dto.getStatus() != null) {
            if (dto.getStatus().equals(StatusUsuarioEnum.ATIVO.get())) {
                dto.setStatusDescricao(StatusAtivoInativo.ATIVO.getTexto());
            } else {
                dto.setStatusDescricao(StatusAtivoInativo.INATIVO.getTexto());
            }
        }

        return dto;
    }

    public static Transportadora paraEntidadeComID(TransportadoraDTO dto) {

        Transportadora t = new Transportadora();

        t.setIdTransportadora(dto.getIdTransportadora());
        t.setCep(dto.getCep());
        t.setCidade(dto.getCidade());
        t.setCnpj(dto.getCnpj());
        t.setEndereco(dto.getEndereco());
        t.setEnvioInformacao(dto.getEnvioInformacao());
        t.setEstado(dto.getEstado());
        t.setNroTelefone(dto.getNroTelefone());
        t.setEmail(dto.getEmail());
        t.setStatus(dto.getStatus());
        t.setIdUsuarioCriacao(dto.getIdUsuarioCriacao());
        t.setDataCriacao(dto.getDataCriacao());
        t.setIdUsuarioAlteracao(dto.getIdUsuarioAlteracao());
        t.setDataAlteracao(dto.getDataAlteracao());
        t.setRazaoSocial(dto.getRazaoSocial());
        t.setResponsavel(dto.getResponsavel());

        return t;
    }
}

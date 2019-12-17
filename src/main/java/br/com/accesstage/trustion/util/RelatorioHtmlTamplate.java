package br.com.accesstage.trustion.util;

import java.util.List;

import br.com.accesstage.trustion.dto.AtividadeDTO;

public class RelatorioHtmlTamplate {

	public static String resumoOcorrencia(List<AtividadeDTO> listaAtividadeDTO) {

		StringBuilder html = new StringBuilder();

		html.append(" <html> ");
		html.append(" <head> ");
		html.append(" </head> ");
		html.append(" <body> ");

		html.append(" <table class=\"table table-bordered\" class=\"table table-striped table-condensed\" cellspacing=\"0\" width=\"100%\"> ");

		html.append(" <thead> ");
		html.append(" <tr class=\"cabecalho\"> ");
		html.append(" <th class=\"text-center texto-branco\"><h5>Usu&aacute;rio</h5></th> ");
		html.append(" <th class=\"text-center texto-branco\"><h5>Data / Hor&aacute;rio</h5></th> ");
		html.append(" <th class=\"text-center texto-branco\"><h5>Atividade</h5></th> ");
		html.append(" </tr> ");
		html.append(" </thead> ");

		html.append(" <tbody> ");

		for (AtividadeDTO atividadeDTO : listaAtividadeDTO) {

			html.append(" <tr> ");
			html.append(" <td class=\"text-center\">" + atividadeDTO.getUsuario() + "</td> ");
			html.append(" <td class=\"text-center\">" + atividadeDTO.getDataHorario() + "</td> ");
			html.append(" <td class=\"text-center\">" + atividadeDTO.getAtividade() + "</td> ");
			html.append(" </tr>");
		}

		html.append(" </tbody>");

		html.append(" </body> ");
		html.append(" </html> ");

		return html.toString();
	}
}

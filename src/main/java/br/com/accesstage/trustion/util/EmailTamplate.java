package br.com.accesstage.trustion.util;

import br.com.accesstage.trustion.EmailProperties;

public class EmailTamplate {

	public static String redefinirSenha(String login, String senha) {

		StringBuilder email = new StringBuilder();

		email.append(" <!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'> ");
		email.append(" <html xmlns='http://www.w3.org/1999/xhtml'> ");
		email.append(" <head> ");
		email.append(" 	<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /> ");
		email.append(" 	<title>Trustion Portal</title> ");
		email.append(" </head> ");
		email.append(" <body> ");
		email.append(" 	<p> ");
		email.append(" 		<b>" + UTF8.Ola + "!</b> ");
		email.append(" 		<br> ");
		email.append(" 		<b>Bem vindo ao Portal de " + UTF8.Conciliacoes + " Financeira!</b> ");
		email.append(" 	</p> ");
		email.append(" 	<p><b>Esses " + UTF8.sao + " seus dados de acesso iniciais:</b></p> ");
		email.append(" 	<p> ");
		email.append(" 		<b>Login: " + login + "</b> ");
		email.append(" 		<br> ");
		email.append(" 		<b>Senha: " + senha + "</b> ");
		email.append(" 	</p> ");
		email.append(" 	<p><b>Por favor, acesse o link abaixo para redefinir a sua senha e realizar o primeiro acesso: </b></p> ");
		email.append(" 	<p><b><a href='" + EmailProperties.getLink() + "'>Redefinir Senha</a></b></p> "); //Mensagem.get("email.link.pagina.redefinir.senha")
		email.append(" 	<img src='" + EmailProperties.getBanner() + "' /> "); //Mensagem.get("email.link.imagem.banner.accesstage.exclusive")
		email.append(" </body> ");

		return email.toString();
	}
	
	public static String envioNotificacao(Long ocorrencia) {

		StringBuilder email = new StringBuilder();

		email.append(" <!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'> ");
		email.append(" <html xmlns='http://www.w3.org/1999/xhtml'> ");
		email.append(" <head> ");
		email.append(" 	<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /> ");
		email.append(" 	<title>Trustion Portal</title> ");
		email.append(" </head> ");
		email.append(" <body> ");
		email.append(" 	<p> ");
		email.append(" 		" + UTF8.Ola + "!");
		email.append(" 		<br> ");
		email.append(" 		Informamos que acaba de ser aberta a "+ UTF8.Ocorrencia +" "+  ocorrencia.toString() + ".Acesse o portal para analisar ");
		email.append(" 	</p> ");
		email.append(" 	<p><b><a href='" + EmailProperties.getLink() + "'>"+ EmailProperties.getLink() +"</a></b></p> "); //Mensagem.get("email.link.pagina.ambiente")
		email.append(" 	<p><b>Atenciosamente </b></p> ");
		email.append(" 	<p><b>Portal de Conciliação Financeira </b></p> ");
		email.append(" 	<img src='" + EmailProperties.getBanner() + "' /> "); //Mensagem.get("email.link.imagem.banner.accesstage.exclusive")
		email.append(" </body> ");

		return email.toString();
	}
}

package br.com.accesstage.trustion.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private static String EMAIL_ORIGEM = "noreply@trustion.com.br";
	private JavaMailSender javaMailSender;

	@Autowired
	public EmailService(JavaMailSender javaMailSender) {

		this.javaMailSender = new JavaMailSenderImpl();
		this.javaMailSender = javaMailSender;
	}

	public void enviarEmail(String emailDestinatario, String titulo, String mensagem) throws MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

		mimeMessageHelper.setFrom(EMAIL_ORIGEM);
		mimeMessageHelper.setTo(emailDestinatario);
		mimeMessageHelper.setSubject(titulo);
		mimeMessageHelper.setText(mensagem, true);

		javaMailSender.send(mimeMessage);
	}
}

package it.os.event.handler.service.impl;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import it.os.event.handler.service.IMailSRV;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailSRV implements IMailSRV {

	@Value("${mail.server-username}")
	private String serverUsername;

	@Value("${mail.server-password}")
	private String serverPassword;

	@Value("${mail.from}")
	private String mailFrom;

	@Value("${mail.to.lower-threshold}")
	private String mailToLower;

	@Value("${mail.to.higher-threshold}")
	private String mailToHigher;

	private JavaMailSender javaMailSender;

	@PostConstruct
	void postConstruct() {
		javaMailSender = createMailSender();
	}

	private JavaMailSender createMailSender() {

		final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername(serverUsername);
		mailSender.setPassword(serverPassword);

		final Properties props = mailSender.getJavaMailProperties();

		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.security.enable", true);
		props.put("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

		return mailSender;
	}

	@Override
	public boolean sendMail(final String subj, final String txtMessage, final boolean isOvertime) {

		boolean isMailSent = false;
		try {

			if (javaMailSender == null) {
				javaMailSender = createMailSender();
			}

			final MimeMessage mm = javaMailSender.createMimeMessage();
			final MimeMessageHelper messageHelper = new MimeMessageHelper(mm, true, "UTF-8");
			messageHelper.setFrom(mailFrom);

			messageHelper.setTo(InternetAddress.parse(mailToLower));
			if (isOvertime) {
				messageHelper.setTo(InternetAddress.parse(mailToHigher));
			}

			messageHelper.setSubject(subj);
			messageHelper.setText(txtMessage, true);
			javaMailSender.send(mm);
			isMailSent = true;
		} catch (final Exception ex) {
			log.error("Error while sending e-mail", ex);
		}
		return isMailSent;
	}

}

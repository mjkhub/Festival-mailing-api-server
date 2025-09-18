package kori.tour.email.application.updater;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailSendService implements EmailSendUseCase{

    @Value("${email.host}")
    private String smtpHost;

    @Value("${email.sender}")
    private String emailSender;

    @Value("${email.password}")
    private String password;


    @Override
    public String sendEmail(List<String> recipients, String title, String content) {
        Session session = getSessionWithSmtpServer();

        for (String recipient :  recipients) {
            try {
                // 메시지 객체 생성
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailSender));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                message.setSubject(title);
                message.setContent(content, "text/html; charset=UTF-8");
                // 메시지 전송
                Transport.send(message);

            } catch (MessagingException e) {
                throw new RuntimeException("Msg error occurred while sending an email ",e);
            }
        }
        return UUID.randomUUID().toString();
    }

    private Session getSessionWithSmtpServer() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSender, password);
            }
        });
    }
}

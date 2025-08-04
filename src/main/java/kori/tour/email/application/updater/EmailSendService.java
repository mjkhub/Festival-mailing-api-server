package kori.tour.email.application.updater;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSendService {

    private final SesV2Client sesClient;

    /**
     * Sends an email with the specified subject and HTML content to a list of recipients using AWS SES.
     *
     * @param emails       the list of recipient email addresses
     * @param emailTitle   the subject of the email
     * @param emailContent the HTML content of the email body
     * @return the message ID assigned by AWS SES for the sent email
     */
    public String sendEmailToMembers(List<String> emails, String emailTitle, String emailContent) {
        SendEmailRequest request = SendEmailRequest.builder()
                .fromEmailAddress("noreply@yourdomain.com")
                .destination(Destination.builder().toAddresses(emails).build())
                .content(EmailContent.builder()
                        .simple(Message.builder()
                                .subject(Content.builder().data(emailTitle).charset("UTF-8").build())
                                .body(Body.builder()
                                        .html(Content.builder().data(emailContent).charset("UTF-8").build())
                                        .build())
                                .build())
                        .build())
                .build();

        SendEmailResponse response = sesClient.sendEmail(request);
        log.info("Email sent, messageId={}", response.messageId());
        return response.messageId();
    }
}

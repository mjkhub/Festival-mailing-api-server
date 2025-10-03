package kori.tour.email.application.updater.client;

import java.util.List;

public interface EmailClient {

    EmailSendResponse sendEmail(List<String> recipients, String title, String content);
}

package kori.tour.email.application.updater;

import java.util.List;

public interface EmailSendUseCase {

    String sendEmail(List<String> recipients, String title, String content);
}

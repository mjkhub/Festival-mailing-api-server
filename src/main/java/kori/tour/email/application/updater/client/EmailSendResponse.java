package kori.tour.email.application.updater.client;

import java.time.LocalDateTime;

public record EmailSendResponse (String messageId, LocalDateTime sendTime){

}

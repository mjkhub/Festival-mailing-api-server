package kori.tour.email.application.updater.dto;

import java.util.List;


public record EmailSendRequest(
        List<String> emails,
        String emailTitle,
        String emailContent,
        String tourId,
        String areaCode,
        String sigunGuCode
) {}

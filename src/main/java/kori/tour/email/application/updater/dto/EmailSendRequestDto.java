package kori.tour.email.application.updater.dto;

import java.util.List;

import kori.tour.member.domain.Member;

public record EmailSendRequestDto(
        List<Member> members,
        String emailTitle,
        String emailContent,
        String tourId,
        String areaCode,
        String sigunGuCode
) {}

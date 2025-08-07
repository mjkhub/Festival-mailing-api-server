package kori.tour.email.application.updater.dto;

import kori.tour.member.domain.Member;

import java.util.List;

public record EmailSendRequestDto(
        List<Member> members,
        String emailTitle,
        String emailContent,
        String tourId,
        String areaCode,
        String sigunGuCode
) {}

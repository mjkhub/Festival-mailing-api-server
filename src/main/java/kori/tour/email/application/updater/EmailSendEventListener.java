package kori.tour.email.application.updater;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kori.tour.email.apdater.out.persistence.EmailRepository;
import kori.tour.email.application.updater.parser.EmailContentParser;
import kori.tour.email.application.updater.parser.dto.EmailBodyDto;
import kori.tour.email.application.updater.parser.dto.EmailTitleDto;
import kori.tour.email.domain.Email;
import kori.tour.keyword.domain.Keyword;
import kori.tour.member.adapter.out.persistence.SubscriptionRepository;
import kori.tour.member.domain.Member;
import kori.tour.member.domain.PlatformInfo;
import kori.tour.member.domain.Subscription;
import kori.tour.tour.adpater.out.persistence.TourRepository;
import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.domain.Tour;
import kori.tour.tour.domain.TourDetail;
import kori.tour.tour.domain.TourRepeat;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailSendEventListener {

    private final EmailContentParser emailContentParser;
    private final EmailSendService emailSendService;
    private final SubscriptionRepository subscriptionRepository;
    private final EmailRepository emailRepository;
    private final TourRepository tourRepository;


    /**
     * Handles an email sending event by composing and sending a new tour notification email to subscribed members.
     *
     * Processes the provided {@link EmailSendEvent} by extracting tour details and associated keywords, generating the email title and body, identifying subscribed members, sending the email to their addresses, and recording the sent email information for each member.
     *
     * This method is executed asynchronously upon event publication.
     *
     * @param emailSendEvent the event containing the new tour information and associated keywords
     */
    @Async // 이거를 커스터마ㅣ징 필요
    @EventListener
    public void keywordExtractingEvent(EmailSendEvent emailSendEvent) {
        // ToDo: 1. Email 형식 2. Email 보내는 로직
        NewTourDto newTourDto = emailSendEvent.entry().getKey();
        List<String> keywordsOfTour = emailSendEvent.entry().getValue().stream().map(Keyword::getKeyword).toList();
        EmailTitleDto emailTitleDto = toEmailTitleDto(newTourDto, keywordsOfTour);
        EmailBodyDto emailBodyDto = toEmailBodyDto(newTourDto, keywordsOfTour);
        String emailTitle = emailContentParser.parseToEmailTitle(emailTitleDto);
        String emailBody = emailContentParser.generateEventEmailHtml(emailBodyDto);

        List<Member> members = subscriptionRepository.findSubscriptionByAreaWithMember(newTourDto.getTour().getAreaCode(), newTourDto.getTour().getSigunGuCode())
                .stream()
                .map(Subscription::getMember)
                .toList();

        List<String> emails = members.stream()
                .map(Member::getPlatformInfo)
                .map(PlatformInfo::getPlatformEmail)
                .toList();

        LocalDateTime sendTime = LocalDateTime.now();
        String messageId = emailSendService.sendEmailToMembers(emails, emailTitle, emailBody);
        Tour tour = tourRepository.save(newTourDto.getTour());

        for (Member member : members) {
            Email email = Email.builder()
                    .member(member)
                    .tour(tour)
                    .sendTime(sendTime)
                    .title(emailTitle)
                    .body(emailBody)
                    .messageId(messageId)
                    .build();
            emailRepository.save(email);
        }

    }


    /**
     * Creates an EmailTitleDto containing the tour's area code, district code, title, and associated keywords.
     *
     * @param newTourDto      the DTO containing tour information
     * @param keywordsOfTour  the list of keywords related to the tour
     * @return an EmailTitleDto populated with the tour's area code, district code, title, and keywords
     */
    private EmailTitleDto toEmailTitleDto(NewTourDto newTourDto, List<String> keywordsOfTour) {
        Tour tour = newTourDto.getTour();

        return new EmailTitleDto(
                tour.getAreaCode(),
                tour.getSigunGuCode(),
                tour.getTitle(),
                keywordsOfTour);
    }

    /**
     * Constructs an EmailBodyDto containing detailed information about a new tour for email content generation.
     *
     * The returned DTO includes the tour's main image, title, event dates, play time, spend time, usage time, associated keywords, repeat information, address, event place, and contact details. If the spend time festival information is missing, "정보 없음" is used as a placeholder.
     *
     * @param newTourDto      the DTO containing tour, detail, and repeat information
     * @param keywordsOfTour  the list of keywords associated with the tour
     * @return an EmailBodyDto populated with the tour's details for email content
     */
    private EmailBodyDto toEmailBodyDto(NewTourDto newTourDto, List<String> keywordsOfTour) {
        Tour tour = newTourDto.getTour();
        TourDetail detail = newTourDto.getDetailInfo().get(0); // 단일 가정
        List<TourRepeat> repeats = newTourDto.getTourRepeatList();

        List<EmailBodyDto.InfoItem> infoList = repeats.stream()
                .map(r -> new EmailBodyDto.InfoItem(r.getInfoName(), r.getInfoText()))
                .toList();

        return new EmailBodyDto(
                tour.getMainImageUrl(),
                tour.getTitle(),
                tour.getEventStartDate(),
                tour.getEventEndDate(),
                detail.getPlayTime(),
                detail.getSpendTimeFestival().isEmpty() ? "정보 없음" : detail.getSpendTimeFestival(),
                detail.getUseTimeFestival(),
                keywordsOfTour,
                infoList,
                tour.getRoadAddress(),
                detail.getEventPlace(),
                tour.getTelephone()
        );
    }


}

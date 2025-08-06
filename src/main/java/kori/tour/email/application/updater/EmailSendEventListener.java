package kori.tour.email.application.updater;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kori.tour.email.adapter.out.persistence.EmailRepository;
import kori.tour.email.application.updater.parser.EmailContentParser;
import kori.tour.email.application.updater.parser.dto.EmailBodyDto;
import kori.tour.email.application.updater.parser.dto.EmailTitleDto;
import kori.tour.email.domain.Email;
import kori.tour.keyword.domain.Keyword;
import kori.tour.member.adapter.out.persistence.MemberRepository;
import kori.tour.member.domain.Member;
import kori.tour.member.domain.PlatformInfo;
import kori.tour.tour.adapter.out.persistence.TourRepository;
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

	private final MemberRepository memberRepository;

	private final EmailRepository emailRepository;

	private final TourRepository tourRepository;

	@Async
	@EventListener
	public void keywordExtractingEvent(EmailSendEvent emailSendEvent) {
		// ToDo: 1. Email 형식 2. Email 보내는 로직 3. Async 스레드 풀 커스터마이징
		NewTourDto newTourDto = emailSendEvent.entry().getKey();
		List<String> keywordsOfTour = emailSendEvent.entry().getValue().stream().map(Keyword::getKeyword).toList();
		EmailTitleDto emailTitleDto = toEmailTitleDto(newTourDto, keywordsOfTour);
		EmailBodyDto emailBodyDto = toEmailBodyDto(newTourDto, keywordsOfTour);
		String emailTitle = emailContentParser.parseToEmailTitle(emailTitleDto);
		String emailBody = emailContentParser.generateEventEmailHtml(emailBodyDto);

		List<Member> members = memberRepository.findBySubscriptionArea(newTourDto.getTour().getAreaCode(),
				newTourDto.getTour().getSigunGuCode());

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

	private EmailTitleDto toEmailTitleDto(NewTourDto newTourDto, List<String> keywordsOfTour) {
		Tour tour = newTourDto.getTour();

		return new EmailTitleDto(tour.getAreaCode(), tour.getSigunGuCode(), tour.getTitle(), keywordsOfTour);
	}

	private EmailBodyDto toEmailBodyDto(NewTourDto newTourDto, List<String> keywordsOfTour) {
		Tour tour = newTourDto.getTour();
		TourDetail detail = newTourDto.getDetailInfo().get(0); // 단일 가정
		List<TourRepeat> repeats = newTourDto.getTourRepeatList();

		List<EmailBodyDto.InfoItem> infoList = repeats.stream()
			.map(r -> new EmailBodyDto.InfoItem(r.getInfoName(), r.getInfoText()))
			.toList();

		return new EmailBodyDto(tour.getMainImageUrl(), tour.getTitle(), tour.getEventStartDate(),
				tour.getEventEndDate(), detail.getPlayTime(),
				detail.getSpendTimeFestival().isEmpty() ? "정보 없음" : detail.getSpendTimeFestival(),
				detail.getUseTimeFestival(), keywordsOfTour, infoList, tour.getRoadAddress(), detail.getEventPlace(),
				tour.getTelephone());
	}

}

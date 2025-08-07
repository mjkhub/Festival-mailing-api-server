package kori.tour.email.application.updater;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kori.tour.email.application.updater.dto.EmailBodyDto;
import kori.tour.email.application.updater.dto.EmailSendRequestDto;
import kori.tour.email.application.updater.dto.EmailTitleDto;
import kori.tour.member.domain.Member;
import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.domain.Tour;
import kori.tour.tour.domain.TourDetail;
import kori.tour.tour.domain.TourRepeat;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailSendEventListener {

	private final EmailContentParser emailContentParser;

	private final EmailService emailService;

	@Async
	@EventListener
	public void listenEmailSendEvent(EmailSendEvent emailSendEvent) {
		// ToDo: Async 스레드 풀 커스터마이징
		NewTourDto newTourDto = emailSendEvent.entry().getKey();
		List<String> keywordsOfTour = emailSendEvent.entry().getValue();

		EmailTitleDto emailTitleDto = mapToEmailTitleDto(newTourDto, keywordsOfTour);
		EmailBodyDto emailBodyDto = mapToEmailBodyDto(newTourDto, keywordsOfTour);

		String emailTitle = emailContentParser.parseToEmailTitle(emailTitleDto);
		String emailBody = emailContentParser.parseToEventEmailHtml(emailBodyDto);

		List<Member> members = emailService.findMembersBySubscription(newTourDto.getTour().getAreaCode(), newTourDto.getTour().getSigunGuCode());
		EmailSendRequestDto requestDto = mapToSendEmailRequestDto(members, emailTitle, emailBody, newTourDto.getTour());
		String messageId = emailService.sendEmailToMembers(requestDto);

		emailService.saveEmails(members, messageId, newTourDto.getTour(), emailTitle, emailBody);
	}

	private EmailTitleDto mapToEmailTitleDto(NewTourDto newTourDto, List<String> keywordsOfTour) {
		Tour tour = newTourDto.getTour();
		return new EmailTitleDto(tour.getAreaCode(), tour.getSigunGuCode(), tour.getTitle(), keywordsOfTour);
	}

	private EmailBodyDto mapToEmailBodyDto(NewTourDto newTourDto, List<String> keywordsOfTour) {
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

	private EmailSendRequestDto mapToSendEmailRequestDto(List<Member> members, String emailTitle, String emailContent, Tour tour) {
		return new EmailSendRequestDto(
				members,
				emailTitle,
				emailContent,
				String.valueOf(tour.getId()),
				tour.getAreaCode(),
				tour.getSigunGuCode()
		);
	}


}

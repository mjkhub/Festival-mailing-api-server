package kori.tour.email.application.updater;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import kori.tour.email.application.updater.dto.EmailBodyDto;
import kori.tour.email.application.updater.dto.EmailSendRequest;
import kori.tour.email.application.updater.dto.EmailTitleDto;
import kori.tour.member.domain.Member;
import kori.tour.tour.application.updater.dto.NewTourDto;
import kori.tour.tour.domain.Tour;
import kori.tour.tour.domain.TourDetail;
import kori.tour.tour.domain.TourRepeat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSendEventListener {

	private final EmailContentParser emailContentParser;

	private final EmailService emailService;

	private final int DB_PAGE_SIZE = 500;

	private final int SES_PAGE_SIZE = 50;

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

		Pageable pageable = PageRequest.of(0,DB_PAGE_SIZE);
		List<String> emailSendMessageIdList = new ArrayList<>();
		int totalMemberCount = 0;
		while (true) {
			Slice<Member> memberPage = emailService.findMembersBySubscription(newTourDto.getTour().getRegionCode().getAreaCode(), newTourDto.getTour().getRegionCode().getSigunGuCode(), pageable);
			for(List<Member> members : partition(memberPage.getContent(), SES_PAGE_SIZE)){
				EmailSendRequest emailSendRequest = mapToSendEmailRequestDto(members, emailTitle, emailBody, newTourDto.getTour());
				String messageId = emailService.sendEmailToMembers(emailSendRequest);
				emailService.saveEmails(members, messageId, newTourDto.getTour(), emailTitle, emailBody);
				emailSendMessageIdList.add(messageId);
				totalMemberCount +=members.size();
			}
			if (!memberPage.hasNext()) break;
			pageable = memberPage.nextPageable();
		}

		log.info("[EmailSendSuccess] 총 {}명의 회원에게 이메일 발송 완료 | Tour ID: {} | Message ID: {} | 지역 코드: {}-{}",
				totalMemberCount,
				newTourDto.getTour().getId(),
				emailSendMessageIdList,
				newTourDto.getTour().getRegionCode().getAreaCode(),
				newTourDto.getTour().getRegionCode().getSigunGuCode());
	}

	private static <T> List<List<T>> partition(List<T> list, int size) {
		List<List<T>> out = new ArrayList<>();
		for (int i = 0; i < list.size(); i += size) {
			out.add(list.subList(i, Math.min(i + size, list.size())));
		}
		return out;
	}

	private EmailTitleDto mapToEmailTitleDto(NewTourDto newTourDto, List<String> keywordsOfTour) {
		Tour tour = newTourDto.getTour();
		return new EmailTitleDto(tour.getRegionCode().getAreaCode(), tour.getRegionCode().getSigunGuCode(), tour.getTitle(), keywordsOfTour);
	}

	private EmailBodyDto mapToEmailBodyDto(NewTourDto newTourDto, List<String> keywordsOfTour) {
		Tour tour = newTourDto.getTour();
		TourDetail detail = newTourDto.getDetailInfo();
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

	private EmailSendRequest mapToSendEmailRequestDto(List<Member> members, String emailTitle, String emailContent, Tour tour) {
		return new EmailSendRequest(
				members,
				emailTitle,
				emailContent,
				String.valueOf(tour.getId()),
				tour.getRegionCode().getAreaCode(),
				tour.getRegionCode().getSigunGuCode()
		);
	}


}

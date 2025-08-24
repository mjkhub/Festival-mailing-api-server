package kori.tour.email.application.updater;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kori.tour.email.adapter.out.persistence.EmailRepository;
import kori.tour.email.application.updater.dto.EmailSendRequestDto;
import kori.tour.email.domain.Email;
import kori.tour.member.adapter.out.persistence.MemberRepository;
import kori.tour.member.domain.Member;
import kori.tour.member.domain.PlatformInfo;
import kori.tour.tour.adapter.out.persistence.TourRepository;
import kori.tour.tour.domain.Tour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

	private final SesV2Client sesClient;

	private final EmailRepository emailRepository;

	private final TourRepository tourRepository;

	private final MemberRepository memberRepository;

	@Value("${email.sender}")
	private String senderEmail;

	public String sendEmailToMembers(EmailSendRequestDto requestDto) {
		List<String> emails = requestDto.members().stream()
				.map(Member::getPlatformInfo)
				.map(PlatformInfo::getPlatformEmail)
				.toList();

		SendEmailRequest request = SendEmailRequest.builder()
				.fromEmailAddress(senderEmail)
				.destination(Destination.builder().toAddresses(emails).build())
				.content(EmailContent.builder()
						.simple(Message.builder()
								.subject(Content.builder().data(requestDto.emailTitle()).charset("UTF-8").build())
								.body(Body.builder().html(Content.builder().data(requestDto.emailContent()).charset("UTF-8").build()).build())
								.build())
						.build())
				.build();

		SendEmailResponse response = sesClient.sendEmail(request);

		log.info("[EmailSendSuccess] 총 {}명의 회원에게 이메일 발송 완료 | Tour ID: {} | Message ID: {} | 지역 코드: {}-{}",
				requestDto.members().size(),
				requestDto.tourId(),
				response.messageId(),
				requestDto.areaCode(),
				requestDto.sigunGuCode()
		);

		return response.messageId();
	}

	public Slice<Member> findMembersBySubscription(String areaCode, String sigunGucCode, Pageable pageRequest){
		return memberRepository.findBySubscriptionArea(areaCode, sigunGucCode, pageRequest);
	}

	@Transactional
	public void saveEmails(List<Member> members, String messageId, Tour tour, String emailTitle, String emailBody) {
		LocalDateTime sendTime = LocalDateTime.now();
		Tour savedTour = tourRepository.save(tour);
		List<Member> savedMembers = memberRepository.saveAll(members);
		for (Member member : savedMembers) {
			Email email = Email.builder()
					.member(member)
					.tour(savedTour)
					.sendTime(sendTime)
					.title(emailTitle)
					.body(emailBody)
					.messageId(messageId)
					.build();
			emailRepository.save(email);
		}
	}

}

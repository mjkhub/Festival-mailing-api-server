package kori.tour.email.application.updater;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kori.tour.email.adapter.out.persistence.EmailContentRepository;
import kori.tour.email.adapter.out.persistence.EmailRepository;
import kori.tour.email.application.updater.client.EmailClient;
import kori.tour.email.application.updater.client.EmailSendResponse;
import kori.tour.email.application.updater.dto.EmailSendRequest;
import kori.tour.email.domain.Email;
import kori.tour.email.domain.EmailContent;
import kori.tour.member.adapter.out.persistence.MemberRepository;
import kori.tour.member.domain.Member;
import kori.tour.tour.adapter.out.persistence.TourRepository;
import kori.tour.tour.domain.Tour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

	private final EmailClient emailClient;
	private final EmailRepository emailRepository;
	private final TourRepository tourRepository;
	private final MemberRepository memberRepository;
	private final EmailContentRepository emailContentRepository;


	public Slice<Member> findMembersBySubscription(String areaCode, String sigunGucCode, Pageable pageRequest){
		return memberRepository.findBySubscriptionArea(areaCode, sigunGucCode, pageRequest);
	}

	public EmailSendResponse sendEmailToMembers(EmailSendRequest requestDto) {
		EmailSendResponse emailSendResponse = emailClient.sendEmail(requestDto.emails(), requestDto.emailTitle(), requestDto.emailContent());
		log.info("Email sent to {} with messageId {}", requestDto.emails(), emailSendResponse.messageId());

		return emailSendResponse;
	}

	@Transactional
	public void saveEmails(List<Member> members, EmailSendResponse emailSendResponse, Tour tour, String emailTitle, String minifiedEmailBody) {
		String messageId = emailSendResponse.messageId();
		LocalDateTime sendTime = emailSendResponse.sendTime();

		EmailContent emailContent = EmailContent.builder()
				.title(emailTitle)
				.body(minifiedEmailBody)
				.messageId(messageId)
				.build();

		Tour savedTour = tourRepository.save(tour);
		EmailContent savedEmailContent = emailContentRepository.save(emailContent);
		List<Member> savedMembers = memberRepository.saveAll(members);

		for (Member member : savedMembers) {
			Email email = Email.builder()
					.member(member)
					.tour(savedTour)
					.sendTime(sendTime)
					.emailContent(savedEmailContent)
					.build();
			emailRepository.save(email);
		}
	}

}

package kori.tour.email.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import kori.tour.member.domain.Member;
import kori.tour.tour.domain.Tour;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "email_id")
	private Long id;

	@JoinColumn(name = "member_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@JoinColumn(name = "tour_id", unique = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Tour tour;

	private LocalDateTime sendTime;

	private String title;

	private String body;

	@Column(name = "ses_message_id", unique = true)
	private String messageId;

}

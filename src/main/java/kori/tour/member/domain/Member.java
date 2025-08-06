package kori.tour.member.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Embedded
	private PlatformInfo platformInfo;

	@Embedded
	private ActivityInfo activityInfo;

	@ElementCollection
	@CollectionTable(name = "member_subscription", joinColumns = @JoinColumn(name = "member_id"))
	private Set<Subscription> subscriptions = new HashSet<>();

	// Todo Subscription을 추가하고 삭제하는 비즈니스 로직

}

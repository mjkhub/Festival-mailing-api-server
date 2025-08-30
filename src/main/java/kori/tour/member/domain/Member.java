package kori.tour.member.domain;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderMethodName = "internalBuilder")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Embedded
	private PlatformInfo platformInfo;

	@Embedded
	private ActivityInfo activityInfo;

	@Embedded
	private Subscriptions subscriptions = new Subscriptions();

	private Boolean emailSubscribe;

	public static MemberBuilder builder() {
		return internalBuilder().subscriptions(new Subscriptions());
	}

	public Set<Subscription> getSubscriptions() {
		return subscriptions.getSubscriptions();
	}

	/**
	 * 중복 Subscription, null 체크 -> 유일성이 깨진 경우에 서비스 계층으로
	 * 예외를 던지는 로직을 고려해봤는데, 결국은 크게 의미가 없는 것 같아.
	 * 프론트에서 순간적으로 꼬이거나, API의 엔드포인트를 알지 않는 이상 발생 x
	 * */
	public void addSubscription(Subscription subscription) {
		this.subscriptions.add(subscription);
	}

	public void removeSubscription(Subscription subscription) {
		this.subscriptions.remove(subscription);
	}

}

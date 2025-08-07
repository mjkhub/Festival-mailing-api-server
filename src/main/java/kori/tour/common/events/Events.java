package kori.tour.common.events;

import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Events {

	private static ApplicationEventPublisher publisher;

	public static void setPublisher(ApplicationEventPublisher publisher) {
		Events.publisher = publisher;
	}

	public static void raise(Object event) {
		if (Objects.nonNull(publisher)) {
			publisher.publishEvent(event);
		}
	}

}

package kori.tour.common.events;

import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Events {

    private static ApplicationEventPublisher publisher;

    /**
     * Sets the ApplicationEventPublisher instance to be used for publishing events.
     *
     * This method must be called before any events can be published using the {@link #raise(Object)} method.
     */
    public static void setPublisher(ApplicationEventPublisher publisher) {
        Events.publisher = publisher;
    }

    /**
     * Publishes the specified event using the configured {@link ApplicationEventPublisher}, if available.
     *
     * If no publisher has been set, this method does nothing.
     *
     * @param event the event object to publish
     */
    public static void raise(Object event) {
        if (Objects.nonNull(publisher)) {
            publisher.publishEvent(event);
        }
    }
}

package kori.tour.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kori.tour.common.events.Events;

@Configuration
public class EventConfig {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Creates an initializing bean that sets the event publisher in the {@code Events} class using the application context.
     *
     * @return an {@code InitializingBean} that configures the event publisher after the Spring context is initialized
     */
    @Bean
    public InitializingBean eventsInitializer() {
        return () -> Events.setPublisher(applicationContext);
    }

}

package com.fouan;

import com.fouan.events.Event;
import com.fouan.events.EventsPublisher;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

import javax.inject.Named;

@Named
@AllArgsConstructor
final class SpringEventPublisher implements EventsPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void fire(Event<?> event) {
        applicationEventPublisher.publishEvent(event);
    }
}

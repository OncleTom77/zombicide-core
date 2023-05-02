package com.fouan.events;

@FunctionalInterface
public interface EventsPublisher {

    void fire(Event<?> event);
}

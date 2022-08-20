package com.fouan

import com.fouan.events.Event
import com.fouan.events.EventsPublisher
import org.springframework.context.ApplicationEventPublisher
import javax.inject.Named

@Named
internal class SpringEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : EventsPublisher {

    override fun fire(event: Event<*>) = applicationEventPublisher.publishEvent(event)
}

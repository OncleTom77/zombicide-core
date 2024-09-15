package com.fouan.events

fun interface EventsPublisher {
    fun fire(event: Event<*>)
}

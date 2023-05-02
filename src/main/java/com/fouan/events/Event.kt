package com.fouan.events

abstract class Event<T : Event<T>>(val turn: Int)

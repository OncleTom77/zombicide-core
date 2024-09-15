package com.fouan.events

abstract class Event<T : Event<T>>(val turn: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event<*>

        return turn == other.turn
    }

    override fun hashCode(): Int {
        return turn
    }
}

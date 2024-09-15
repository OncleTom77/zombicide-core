package com.fouan.util

import java.util.*
import kotlin.collections.ArrayDeque

class Deck<T>(cards: List<T>) {
    protected val cards: ArrayDeque<T>
    private val discard: MutableList<T>

    init {
        this.cards = ArrayDeque(cards)
        discard = ArrayList()
    }

    fun shuffle() {
        Collections.shuffle(cards)
    }

    fun drawCard(): T {
        if (cards.isEmpty()) {
            refill()
        }
        val card = cards.removeFirst()
        discard.add(card)
        return card
    }

    fun refill() {
        cards.addAll(discard)
        discard.clear()
        shuffle()
    }

    override fun toString(): String {
        return "Deck: " + cards.joinToString(",") { obj ->
            obj.toString()
        }
    }
}

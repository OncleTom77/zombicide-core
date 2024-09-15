package com.fouan.spawn

import com.fouan.util.Deck

class ZombieSpawnCardDeck {
    private val deck: Deck<ZombieSpawnCard>

    init {
        val zombieSpawnCards = (1..10).map { ZombieSpawnCard.WalkerSpawnCard }
        deck = Deck(zombieSpawnCards)
    }

    fun drawCard(): ZombieSpawnCard = deck.drawCard()
}
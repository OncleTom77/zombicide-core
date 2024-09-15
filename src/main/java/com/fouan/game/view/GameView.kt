package com.fouan.game.view

import com.fouan.actors.ActorId
import com.fouan.events.Event

interface GameView {
    val currentTurn: Int
    fun fireEvent(event: Event<*>)
    fun rollDice(): Int
    fun isTurnEnded(actorId: ActorId): Boolean
    val isGameDone: Boolean
    fun rollbackTurn(turn: Int)
}

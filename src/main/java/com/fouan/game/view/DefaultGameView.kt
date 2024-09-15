package com.fouan.game.view

import com.fouan.actors.ActorId
import com.fouan.actors.view.ActorsCommands
import com.fouan.actors.view.ActorsQueries
import com.fouan.dice.DiceRoller
import com.fouan.events.*
import com.fouan.zones.view.ZonesCommands
import org.springframework.context.event.EventListener
import javax.inject.Named

@Named
internal class DefaultGameView(
    private val actorsCommands: ActorsCommands,
    private val actorsQueries: ActorsQueries,
    private val zonesCommands: ZonesCommands,
    private val eventsPublisher: EventsPublisher,
    private val diceRoller: DiceRoller
) : GameView {
    private val gameHistory: ArrayDeque<GameEvent> = ArrayDeque()
    private val history: ArrayDeque<Event<*>> = ArrayDeque()

    @EventListener
    fun handleGameEvents(event: GameEvent) {
        gameHistory.addFirst(event)
    }

    @EventListener
    fun handleAllEvents(event: Event<*>) {
        history.add(event)
    }

    @EventListener
    fun handleSurvivorDied(event: SurvivorDied) {
        if (actorsQueries.allLivingSurvivors().isEmpty()) {
            fireEvent(SurvivorsDefeated(event.turn))
        }
    }

    override val currentTurn: Int
        get() = gameHistory.firstOrNull()?.turn ?: 0

    override fun fireEvent(event: Event<*>) {
        eventsPublisher.fire(event)
    }

    override fun rollDice(): Int {
        return diceRoller.roll()
    }

    override fun isTurnEnded(actorId: ActorId): Boolean {
        val turn = currentTurn
        return history.filterIsInstance<SurvivorsTurnEnded>()
            .any { it.actorId == actorId && it.turn == turn }
    }

    override val isGameDone: Boolean
        get() = gameHistory.any { it is EndGameEvent }

    override fun rollbackTurn(turn: Int) {
        val eventsToRestore = history.filter { hist -> hist.turn < turn }
        actorsCommands.clear()
        zonesCommands.clear()
        gameHistory.clear()
        history.clear()
        eventsToRestore.forEach { eventsPublisher.fire(it) }
    }
}

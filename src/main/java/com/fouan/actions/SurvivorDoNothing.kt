package com.fouan.actions

import com.fouan.actors.view.ActorsQueries
import com.fouan.events.ActionChosen
import com.fouan.events.SurvivorsTurnEnded
import com.fouan.game.view.GameView
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SurvivorDoNothing(
    private val gameView: GameView,
    private val actorsQueries: ActorsQueries
) : SurvivorAction {

    @EventListener
    fun handleActionChosen(event: ActionChosen) {
        if (event.action !== Actions.NOTHING) return

        val survivorId = actorsQueries.findCurrentSurvivorIdForTurn(event.turn)
            .orElseThrow()
        gameView.fireEvent(SurvivorsTurnEnded(event.turn, survivorId))
    }

    override fun getAction(): Actions {
        return Actions.NOTHING
    }

    override fun isPossible(): Boolean {
        return true
    }
}
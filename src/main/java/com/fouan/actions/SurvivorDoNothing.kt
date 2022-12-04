package com.fouan.actions

import com.fouan.actors.view.ActorsView
import com.fouan.events.ActionChosen
import com.fouan.events.SurvivorsTurnEnded
import com.fouan.game.view.GameView
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SurvivorDoNothing(
        private val gameView: GameView,
        private val actorsView: ActorsView
) : Action {

    @EventListener
    fun handleActionChosen(event: ActionChosen) {
        if (event.action !== Actions.NOTHING) return

        val survivorId = actorsView.findCurrentSurvivorIdForTurn(event.turn)
                .orElseThrow()
        gameView.fireEvent(SurvivorsTurnEnded(event.turn, survivorId))
    }

}
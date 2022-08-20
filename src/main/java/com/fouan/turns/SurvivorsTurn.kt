package com.fouan.turns

import com.fouan.events.SurvivorsTurnEnded
import com.fouan.events.SurvivorsTurnStarted
import com.fouan.game.view.GameView

internal class SurvivorsTurn(
    private val gameView: GameView,
) : Turn {

    override fun play() {
        startSurvivorsTurn()

        // Foreach survivor
        // 1 Define available actions
        // 2 Wait for selected action from displayer
        // 3 Execute action

        endSurvivorsTurn()
    }

    private fun startSurvivorsTurn() {
        val newTurn = gameView.currentTurn + 1
        gameView.fireEvent(SurvivorsTurnStarted(newTurn))
    }

    private fun endSurvivorsTurn() = gameView.fireEvent(SurvivorsTurnEnded(gameView.currentTurn))
}

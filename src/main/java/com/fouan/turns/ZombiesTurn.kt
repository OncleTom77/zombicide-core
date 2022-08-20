package com.fouan.turns

import com.fouan.events.ZombiesTurnEnded
import com.fouan.events.ZombiesTurnStarted
import com.fouan.game.view.GameView

internal class ZombiesTurn(
    private val gameView: GameView,
) : Turn {

    override fun play() {
        startZombiesTurn()
        endZombiesTurn()
    }

    private fun startZombiesTurn() = gameView.fireEvent(ZombiesTurnStarted(gameView.currentTurn))

    private fun endZombiesTurn() = gameView.fireEvent(ZombiesTurnEnded(gameView.currentTurn))
}

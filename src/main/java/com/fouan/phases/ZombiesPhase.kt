package com.fouan.phases

import com.fouan.events.ZombiesTurnEnded
import com.fouan.events.ZombiesTurnStarted
import com.fouan.game.view.GameView

internal class ZombiesPhase(
    private val gameView: GameView,
) : Phase {

    override fun play() {
        startZombiesTurn()
        endZombiesTurn()
    }

    private fun startZombiesTurn() = gameView.fireEvent(ZombiesTurnStarted(gameView.currentTurn))

    private fun endZombiesTurn() = gameView.fireEvent(ZombiesTurnEnded(gameView.currentTurn))
}

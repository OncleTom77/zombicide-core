package com.fouan.game

import com.fouan.events.GameStarted
import com.fouan.game.view.GameView
import com.fouan.phases.Phase
import kotlinx.datetime.Clock
import javax.inject.Named

@Named
class Game(
    private val gameView: GameView,
    @param:Named("initializationPhase") private val initializePhase: Phase,
    private val round: Round
) {
    fun run() {
        gameView.fireEvent(GameStarted(gameView.currentTurn, Clock.System.now().toEpochMilliseconds()))
        initializePhase.play()
        round.start()
    }
}

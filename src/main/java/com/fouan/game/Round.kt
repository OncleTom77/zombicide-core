package com.fouan.game

import com.fouan.game.view.GameView
import com.fouan.phases.SurvivorsPhase
import com.fouan.phases.ZombiesPhase
import mu.KotlinLogging
import javax.inject.Named

@Named
class Round(
    private val gameView: GameView,
    private val survivorsPhase: SurvivorsPhase,
    private val zombiesPhase: ZombiesPhase,
) {
    private val logger = KotlinLogging.logger { }

    fun start() {

        while (!gameView.isGameDone) {
            logger.info { "Start round" }
            survivorsPhase.play()
            zombiesPhase.play()
            logger.info { "End round" }
        }
    }
}
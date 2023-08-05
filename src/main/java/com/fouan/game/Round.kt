package com.fouan.game

import com.fouan.actors.view.ActorsQueries
import com.fouan.actors.view.SurvivorToken
import com.fouan.events.SurvivorTokenAdded
import com.fouan.events.SurvivorTokenRemoved
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
    private val actorsQueries: ActorsQueries
) {
    private val logger = KotlinLogging.logger { }

    fun start() {

        while (!gameView.isGameDone) {
            logger.info { "Start round" }
            survivorsPhase.play()

            if (gameView.isGameDone) {
                break
            }

            zombiesPhase.play()
            endPhase()
            logger.info { "End round" }
        }
    }

    private fun endPhase() {
        updateFirstPlayer()
    }

    private fun updateFirstPlayer() {
        val survivors = actorsQueries.allLivingSurvivors()
        val firstPlayerIndex = survivors.indexOfFirst { it.tokens.contains(SurvivorToken.FIRST_PLAYER) }
        val nextFirstPlayerIndex = (firstPlayerIndex + 1) % survivors.size

        gameView.fireEvent(
            SurvivorTokenRemoved(
                gameView.currentTurn,
                survivors[firstPlayerIndex].id,
                SurvivorToken.FIRST_PLAYER
            )
        )
        gameView.fireEvent(
            SurvivorTokenAdded(
                gameView.currentTurn,
                survivors[nextFirstPlayerIndex].id,
                SurvivorToken.FIRST_PLAYER
            )
        )
    }
}
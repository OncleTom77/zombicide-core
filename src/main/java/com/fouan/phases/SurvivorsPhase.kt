package com.fouan.phases

import com.fouan.actions.Actions
import com.fouan.actions.SurvivorAction
import com.fouan.actors.ActorId
import com.fouan.actors.Survivor
import com.fouan.actors.view.ActorsQueries
import com.fouan.events.AvailableActionsDefined
import com.fouan.events.SurvivorsTurnEnded
import com.fouan.events.SurvivorsTurnStarted
import com.fouan.game.view.GameView
import mu.KotlinLogging
import javax.inject.Named

@Named
class SurvivorsPhase(
    private val gameView: GameView,
    private val actorsQueries: ActorsQueries,
    private val actions: List<SurvivorAction>
) : Phase {

    private val logger = KotlinLogging.logger { }

    override fun play() {
        val turn = gameView.currentTurn + 1

        actorsQueries.allLivingSurvivors()
            .forEach {
                startSurvivorTurn(it.id, turn)

                while (!gameView.isTurnEnded(it.id)) {
                    val possibleActions = getPossibleActions()
                    gameView.fireEvent(AvailableActionsDefined(turn, possibleActions))

                    if (actorsQueries.getRemainingActionsCountForActor(it.id, turn) == 0) {
                        endSurvivorTurn(it, turn)
                    }
                    //TODO: check survivor win
                }
            }
    }

    private fun getPossibleActions(): List<Actions> {
        return actions.filter { it.isPossible() }
            .map { it.getAction() }
    }

    private fun startSurvivorTurn(survivorId: ActorId, turn: Int) {
        gameView.fireEvent(SurvivorsTurnStarted(turn, survivorId))
    }

    private fun endSurvivorTurn(survivor: Survivor, turn: Int) {
        gameView.fireEvent(SurvivorsTurnEnded(turn, survivor.id))
    }
}

package com.fouan.phases

import com.fouan.actions.Actions
import com.fouan.actors.ActorId
import com.fouan.actors.Survivor
import com.fouan.actors.view.ActorsView
import com.fouan.events.AvailableActionsDefined
import com.fouan.events.SurvivorsTurnEnded
import com.fouan.events.SurvivorsTurnStarted
import com.fouan.game.view.GameView
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import javax.inject.Named
import kotlin.math.log

@Named
class SurvivorsPhase(
    private val gameView: GameView,
    private val actorsView: ActorsView,
) : Phase {

    private val logger = KotlinLogging.logger { }

    override fun play() {

        val turn = gameView.currentTurn + 1

        // List survivors
        actorsView.allLivingSurvivors()
            .forEach {
                startSurvivorTurn(it.id, turn)

                while (actorsView.findRemainingActionsForSurvivor(it, turn) > 0) {
                    val possibleActions = getPossibleActions()
                    gameView.fireEvent(AvailableActionsDefined(turn, possibleActions))
                }
                // Loop action count <= 3
                // Actions chooser
                // wait for player action choice
                // action.play()

                endSurvivorTurn(it, turn)
            }
        // foreach survivor start turn
        // 1 Define available actions
        // 2 Wait for selected action from displayer
        // 3 Execute action

        System.exit(0);

        // Foreach survivor
        // 1 Define available actions
        // 2 Wait for selected action from displayer
        // 3 Execute action
    }

    private fun getPossibleActions(): List<Actions>  {
        return Actions.values().toList();
    }

    private fun startSurvivorTurn(survivorId: ActorId, turn: Int) {
        gameView.fireEvent(SurvivorsTurnStarted(turn, survivorId))
    }

    private fun endSurvivorTurn(survivor: Survivor, turn: Int) = gameView.fireEvent(SurvivorsTurnEnded(turn, survivor))
}

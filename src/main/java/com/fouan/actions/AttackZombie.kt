package com.fouan.actions

import com.fouan.actors.view.ActorsQueries
import com.fouan.dice.DiceRoller
import com.fouan.events.*
import com.fouan.game.view.GameView
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AttackZombie(
    private val gameView: GameView,
    private val actorsQueries: ActorsQueries,
    private val diceRoller: DiceRoller
) : SurvivorAction {

    private val logger = KotlinLogging.logger { }

    @EventListener
    fun handleActionChosen(event: ActionChosen) {
        if (event.action !== Actions.COMBAT) return

        val survivor = actorsQueries.findCurrentSurvivorIdForTurn(event.turn)
            .flatMap { actorsQueries.findLivingSurvivorBy(it) }
            .orElseThrow()

        // TODO: choose among all survivor's weapons
        val attackResult = survivor.weapon.use(diceRoller)
        gameView.fireEvent(SurvivorAttacked(event.turn, survivor.id, attackResult))

        if (attackResult.hitCount > 0) {
            val zombies = actorsQueries.findAllZombiesOnSameZoneAsSurvivor(survivor.id)
                .filter { attackResult.weapon.damage >= it.minDamageToDestroy }
                .toSet()

            gameView.fireEvent(AvailableZombiesForSurvivorAttackDefined(event.turn, zombies, attackResult.hitCount))
        }
    }

    @EventListener
    fun handleZombiesChosen(event: ZombiesChosen) {
        val survivor = actorsQueries.findCurrentSurvivorIdForTurn(event.turn)
            .flatMap { actorsQueries.findLivingSurvivorBy(it) }
            .orElseThrow()

        event.chosenZombies.forEach {
            gameView.fireEvent(
                ZombieDied(
                    event.turn,
                    it.id,
                    survivor.id,
                    survivor.weapon
                )
            )
        }
    }

    override fun getAction(): Actions {
        return Actions.COMBAT
    }

    override fun isPossible(): Boolean {
        val survivorId = actorsQueries.findCurrentSurvivorIdForTurn(gameView.currentTurn)
            .orElseThrow()
        // TODO: With distance weapons, check for each survivor's equipped weapon if a zombie is in range
        return actorsQueries.findAllZombiesOnSameZoneAsSurvivor(survivorId)
            .isNotEmpty()
    }
}
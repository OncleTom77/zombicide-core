package com.fouan.actions

import com.fouan.actors.view.ActorsView
import com.fouan.actors.zombies.Zombie
import com.fouan.dice.DiceRoller
import com.fouan.events.ActionChosen
import com.fouan.events.AvailableZombiesForSurvivorAttackDefined
import com.fouan.events.ZombieDied
import com.fouan.events.ZombiesChosen
import com.fouan.game.view.GameView
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AttackZombie(
        private val gameView: GameView,
        // TODO: Replace ActorsView by ActorsQueries
        private val actorsView: ActorsView,
        private val diceRoller: DiceRoller
) : Action {

    private val logger = KotlinLogging.logger { }
    @EventListener
    fun handleActionChosen(event: ActionChosen) {
        if (event.action !== Actions.COMBAT) return

        val survivor = actorsView.findCurrentSurvivorIdForTurn(event.turn)
                .flatMap { actorsView.findSurvivorBy(it) }
                .orElseThrow()

        // TODO: choose among all survivor's weapons
        val attackResult = survivor.weapon.use(diceRoller)
        // TODO: send attack result dice event

        if (attackResult.hitCount > 0) {
            val zombies = actorsView.findCurrentSurvivorIdForTurn(event.turn)
                    .map { actorsView.findAllZombiesOnSameZoneAsSurvivor(it) }
                    .orElseThrow()
                    .filter { attackResult.weapon.damage >= it.minDamageToDestroy }
                    .toSet()

            gameView.fireEvent(AvailableZombiesForSurvivorAttackDefined(event.turn, zombies, attackResult.hitCount))
        }
    }

    @EventListener
    fun handleZombiesChosen(event: ZombiesChosen) {
        val survivor = actorsView.findCurrentSurvivorIdForTurn(event.turn)
                .flatMap { actorsView.findSurvivorBy(it) }
                .orElseThrow()

        event.chosenZombies.forEach {zombie: Zombie -> gameView.fireEvent(ZombieDied(event.turn, zombie.id, survivor.id, survivor.weapon)) }
    }
}
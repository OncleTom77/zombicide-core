package com.fouan.phases

import com.fouan.actors.Actor
import com.fouan.actors.Survivor
import com.fouan.actors.view.ActorsQueries
import com.fouan.actors.zombies.Zombie
import com.fouan.events.ZombiesTurnEnded
import com.fouan.events.ZombiesTurnStarted
import com.fouan.game.view.GameView
import com.fouan.zones.Zone
import com.fouan.zones.view.ZonesQueries
import javax.inject.Named

@Named
class ZombiesPhase(
    private val gameView: GameView,
    private val actorsQueries: ActorsQueries,
    private val zonesQueries: ZonesQueries
) : Phase {

    override fun play() {
        startZombiesTurn()

        activationStep()
        spawnStep()

        endZombiesTurn()
    }

    private fun activationStep() {
        while (!actorsQueries.allZombieActionsSpent()) {
            // find Zombies that can attack survivors
            val attackingZombies = actorsQueries.findAttackingZombies()
            zonesQueries.findAll()
                .forEach { zone ->
                    val actors: Map<Class<out Actor>, List<Actor>> = zonesQueries.findActorIdsOn(zone.position)
                        .map { actorsQueries.findActorBy(it) }
                        .groupBy { it.javaClass }

                    if (actors.containsKey(Survivor::class.java)
                        && actors.containsKey(Zombie::class.java)) {
                        handleZombieAttack(zone, actors)
                    }
                }

            //  - play Zombies, zone by zone, attacking altogether the survivors
            //      - ask player which survivor should be the target of the attacks
            // find remaining zombies and move them toward the noisiest zones
        }
    }

    private fun handleZombieAttack(zone: Zone, actors: Map<Class<out Actor>, List<Actor>>) {
        val survivors = actors[Survivor::class.java]!!
        val zombies = actors[Zombie::class.java]!!

        // TODO: ask player which survivor should be the target of the attacks
    }

    private fun spawnStep() {
        TODO("Not yet implemented")
    }

    private fun startZombiesTurn() = gameView.fireEvent(ZombiesTurnStarted(gameView.currentTurn))

    private fun endZombiesTurn() = gameView.fireEvent(ZombiesTurnEnded(gameView.currentTurn))
}

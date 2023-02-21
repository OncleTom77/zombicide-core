package com.fouan.phases

import com.fouan.actors.Actor
import com.fouan.actors.Survivor
import com.fouan.actors.view.ActorsQueries
import com.fouan.actors.zombies.Zombie
import com.fouan.events.*
import com.fouan.game.view.GameView
import com.fouan.old.board.ZoneUtils
import com.fouan.zones.Zone
import com.fouan.zones.view.ZonesQueries
import org.springframework.context.event.EventListener
import javax.inject.Named
import kotlin.reflect.KClass

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
        val defaultNoisiestZones = zonesQueries.findNoisiestZones()

        do {
            var zombiePlayed = false
            zonesQueries.findAll()
                .forEach { zone ->
                    // TODO: game can be lost here, do not try to activate more zombies (listen to end game event and check here if the game is lost before doing anything else)

                    val actors: Map<KClass<out Actor>, List<Actor>> = zonesQueries.findActorIdsOn(zone.position)
                        .map { actorsQueries.findActorBy(it) }
                        .groupBy { actor ->
                            when (actor) {
                                is Zombie -> return@groupBy Zombie::class
                                is Survivor -> return@groupBy Survivor::class
                                else -> throw IllegalStateException()
                            }
                        }

                    if (actors.containsKey(Zombie::class)) {
                        val zombiesWithRemaingActions = actors[Zombie::class]!!
                            .filter { actorsQueries.getRemainingActionsCountForActor(it.id, gameView.currentTurn) > 0 }

                        if (zombiesWithRemaingActions.isNotEmpty()) {
                            if (actors.containsKey(Survivor::class)) {
                                val survivors = actors[Survivor::class]!!
                                handleZombieAttack(zone, zombiesWithRemaingActions, survivors)
                            } else {
                                handleZombieMove(zone, zombiesWithRemaingActions, defaultNoisiestZones)
                            }
                            zombiePlayed = true
                        }
                    }
                }
        } while (zombiePlayed)
    }

    private fun handleZombieAttack(
        zone: Zone,
        zombies: List<Actor>,
        survivors: List<Actor>
    ) {
        zombies.forEach { gameView.fireEvent(ZombieAttacked(gameView.currentTurn, it.id)) }

        gameView.fireEvent(
            AvailableSurvivorsForZombiesAttackDefined(
                gameView.currentTurn,
                zone.position,
                zombies.map { it.id },
                survivors.map { it.id }
            )
        )
    }

    private fun handleZombieMove(zone: Zone, zombies: List<Actor>, defaultNoisiestZones: List<Zone>) {
        val noisiestZonesInSight = ZoneUtils.getNoisiestZones(zone.getAllInSight(), true)
    }

    @EventListener
    fun handleZombieAttacksDistributed(event: ZombieAttacksDistributed) {
        event.distribution.forEach {
            it.value.forEach { zombie ->
                gameView.fireEvent(SurvivorLostLifePoints(event.turn, it.key.id, zombie.id, zombie.damage))
            }
        }
    }

    private fun spawnStep() {
        TODO("Not yet implemented")
    }

    private fun startZombiesTurn() = gameView.fireEvent(ZombiesTurnStarted(gameView.currentTurn))

    private fun endZombiesTurn() = gameView.fireEvent(ZombiesTurnEnded(gameView.currentTurn))
}

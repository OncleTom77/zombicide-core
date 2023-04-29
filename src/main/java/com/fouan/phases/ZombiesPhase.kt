package com.fouan.phases

import com.fouan.actors.Actor
import com.fouan.actors.ActorId
import com.fouan.actors.Survivor
import com.fouan.actors.view.ActorsQueries
import com.fouan.actors.zombies.Walker
import com.fouan.actors.zombies.Zombie
import com.fouan.algorithm.pathfinding.ZombicidePathFinder
import com.fouan.events.*
import com.fouan.game.view.GameView
import com.fouan.zones.Zone
import com.fouan.zones.view.ZonesQueries
import org.springframework.context.event.EventListener
import javax.inject.Named
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Named
class ZombiesPhase(
    private val gameView: GameView,
    private val actorsQueries: ActorsQueries,
    private val zonesQueries: ZonesQueries,
    private val pathFinder: ZombicidePathFinder,
) : Phase {

    override fun play() {
        startZombiesTurn()

        activationStep()
        spawnStep()

        endZombiesTurn()
    }

    private fun activationStep() {
        playZombiesThatCanAttack()

        if (!gameView.isGameDone) {
            playZombiesThatMustMove()
        }
    }

    private fun playZombiesThatCanAttack() {
        zonesQueries.findAll()
            .forEach { zone ->
                // TODO: game can be lost here, do not try to activate more zombies (listen to end game event and check here if the game is lost before doing anything else)

                val actors = getActorsOnZoneGroupedByType(zone)

                if (actors.containsKey(Zombie::class) && actors.containsKey(Survivor::class)) {
                    val survivors = actors[Survivor::class]!!
                    val zombiesWithRemainingActions = actors[Zombie::class]!!
                        .map { it as Zombie }
                        .filter { actorsQueries.getRemainingActionsCountForActor(it.id, gameView.currentTurn) > 0 }

                    if (zombiesWithRemainingActions.isNotEmpty()) {
                        handleZombieAttack(zone, zombiesWithRemainingActions, survivors)
                    }
                }
            }
    }

    private fun playZombiesThatMustMove() {
        val defaultNoisiestZones = zonesQueries.findNoisiestZones(false)

        zonesQueries.findAll()
            .forEach { zone ->
                // TODO: game can be lost here, do not try to activate more zombies (listen to end game event and check here if the game is lost before doing anything else)

                val actors: Map<KClass<out Actor>, List<Actor>> = getActorsOnZoneGroupedByType(zone)

                if (actors.containsKey(Zombie::class) && !actors.containsKey(Survivor::class)) {
                    val zombiesWithRemainingActions: List<Zombie> = actors[Zombie::class]!!
                        .map { it as Zombie }
                        .filter { actorsQueries.getRemainingActionsCountForActor(it.id, gameView.currentTurn) > 0 }

                    if (zombiesWithRemainingActions.isNotEmpty()) {
                        handleZombieMove(zone, zombiesWithRemainingActions, defaultNoisiestZones)
                    }
                }
            }
    }

    private fun getActorsOnZoneGroupedByType(zone: Zone): Map<KClass<out Actor>, List<Actor>> {
        return zonesQueries.findActorIdsOn(zone.position)
            .map { actorsQueries.findActorBy(it) }
            .groupBy {
                if (it::class.isSubclassOf(Zombie::class)) Zombie::class
                else Survivor::class
            }
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

    private fun handleZombieMove(zone: Zone, zombies: List<Zombie>, defaultNoisiestZones: List<Zone>) {
        // TODO:
        //  - get noisiest zones of all zones
        //  - for each zombie:
        //    - get noisiest zones with survivors in sight, if no such zone exists, get the default noisiest zone
        //    - find all possible next zones of all shortest paths to go to the noisiest zone
        //    - split zombies in equal groups and generate new zombies if necessary
        //    - make them move to their destination zone

        val noisiestZonesInSight = zonesQueries.findNoisiestZonesInSight(zone)
        val destinationZones = if (noisiestZonesInSight.isEmpty()) defaultNoisiestZones else noisiestZonesInSight

        val nextZones = destinationZones.map { pathFinder.findNextZoneOfAllShortestPaths(zone, it) }
            .flatten()
            .toSet()
        splitZombiesInEqualGroups(zombies, nextZones)
    }

    private fun splitZombiesInEqualGroups(zombies: List<Zombie>, nextZones: Set<Zone>) {
        zombies.groupBy { it.javaClass.kotlin }
            .forEach {
                val remainingZombies = ArrayDeque(it.value)
                while (remainingZombies.isNotEmpty()) {
                    nextZones.forEach { zone ->
                        if (remainingZombies.isEmpty()) {
                            val newZombie = when (it.key) {
                                Walker::class -> Walker(ActorId())
                                else -> throw NotImplementedError()
                            }
                            gameView.fireEvent(
                                ZombieSpawned(
                                    gameView.currentTurn,
                                    newZombie,
                                    zone
                                )
                            )
                        } else {
                            gameView.fireEvent(
                                ZombieMoved(
                                    gameView.currentTurn,
                                    remainingZombies.removeFirst().id,
                                    zone.position
                                )
                            )
                        }
                    }
                }
            }
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
//        TODO("Not yet implemented")
    }

    private fun startZombiesTurn() = gameView.fireEvent(ZombiesTurnStarted(gameView.currentTurn + 1))

    private fun endZombiesTurn() = gameView.fireEvent(ZombiesTurnEnded(gameView.currentTurn))
}

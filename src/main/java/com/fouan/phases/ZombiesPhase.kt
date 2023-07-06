package com.fouan.phases

import com.fouan.actors.Actor
import com.fouan.actors.ActorId
import com.fouan.actors.DangerLevel
import com.fouan.actors.Survivor
import com.fouan.actors.view.ActorsQueries
import com.fouan.actors.zombies.Walker
import com.fouan.actors.zombies.Zombie
import com.fouan.algorithm.pathfinding.ZombicidePathFinder
import com.fouan.events.*
import com.fouan.game.view.GameView
import com.fouan.spawn.ZombieSpawnCardDeck
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

    private val zombieSpawnCardDeck = ZombieSpawnCardDeck()

    override fun play() {
        startZombiesTurn()

        activationStep()

        if (!gameView.isGameDone) {
            spawnStep()
        }

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
        val noisiestZonesInSight = zonesQueries.findNoisiestZonesInSight(zone)
        val destinationZones = noisiestZonesInSight.ifEmpty { defaultNoisiestZones }

        val nextZones = destinationZones.map { pathFinder.findNextZoneOfAllShortestPaths(zone, it) }
            .flatten()
            .toSet()
        splitZombiesInEqualGroups(zombies, nextZones)
    }

    private fun splitZombiesInEqualGroups(zombies: List<Zombie>, nextZones: Set<Zone>) {
        zombies.groupBy { it::class }
            .forEach {
                val remainingZombies = ArrayDeque(it.value)
                while (remainingZombies.isNotEmpty()) {
                    nextZones.forEach { zone ->
                        if (remainingZombies.isEmpty()) {
                            generateZombie(it.key, zone)
                        } else {
                            moveZombie(remainingZombies.removeFirst(), zone)
                        }
                    }
                }
            }
    }

    private fun generateZombie(zombieType: KClass<out Zombie>, zone: Zone) {
        val newZombie = when (zombieType) {
            Walker::class -> Walker(ActorId())
            else -> throw NotImplementedError()
        }
        gameView.fireEvent(ZombieSpawned(gameView.currentTurn, newZombie, zone))
    }

    private fun moveZombie(zombie: Zombie, zone: Zone) {
        gameView.fireEvent(ZombieMoved(gameView.currentTurn, zombie.id, zone.position))
    }

    @EventListener
    fun handleZombieAttacksDistributed(event: ZombieAttacksDistributed) {
        event.distribution.forEach {
            gameView.fireEvent(
                SurvivorLostLifePoints(
                    event.turn,
                    it.key.id,
                    it.value.map { zombie -> zombie.id },
                    it.value.sumOf { zombie -> zombie.damage }
                )
            )
        }
    }

    private fun spawnStep() {
        val currentDangerLevel = actorsQueries.getCurrentDangerLevel()
        zonesQueries.findByMarker(Zone.ZoneMarker.ZOMBIE_SPAWN)
            .forEach { spawnZombieOnZone(it, currentDangerLevel) }
    }

    private fun spawnZombieOnZone(zone: Zone, currentDangerLevel: DangerLevel) {
        zombieSpawnCardDeck.drawCard()
            .getSpawnInfo(currentDangerLevel)
            ?.let {
                (1..it.quantity).forEach { _ ->
                    generateZombie(it.type, zone)
                }
            }
    }

    private fun startZombiesTurn() = gameView.fireEvent(ZombiesTurnStarted(gameView.currentTurn + 1))

    private fun endZombiesTurn() = gameView.fireEvent(ZombiesTurnEnded(gameView.currentTurn))
}

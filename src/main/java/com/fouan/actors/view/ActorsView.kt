package com.fouan.actors.view

import com.fouan.actors.Actor
import com.fouan.actors.ActorId
import com.fouan.actors.DangerLevel
import com.fouan.actors.Survivor
import com.fouan.actors.zombies.Zombie
import com.fouan.events.*
import com.fouan.zones.Zone
import com.fouan.zones.view.ZonesQueries
import org.springframework.context.event.EventListener
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Named

@Named
class ActorsView(private val zonesQueries: ZonesQueries, private val eventsPublisher: EventsPublisher) : ActorsCommands,
    ActorsQueries {
    private val history: ArrayDeque<ActorEvent> = ArrayDeque()
    private val actors = Actors()

    @EventListener
    fun handleActorEvents(event: ActorEvent) {
        history.add(event)
    }

    @EventListener
    fun handleSurvivorAdded(event: SurvivorAdded) {
        actors.add(event.survivor)
    }

    @EventListener
    fun handleZombieSpawn(event: ZombieSpawned) {
        actors.add(event.zombie)
    }

    @EventListener
    fun handleZombieDied(event: ZombieDied) {
        val zombie = findZombieBy(event.zombie.id)!!
        // TODO: 28/01/2023 Should we add methods in ActorsCommands to add/remove actors?
        actors.remove(zombie)
        eventsPublisher.fire(
            SurvivorGainedExperience(event.turn, event.actorId, zombie.experienceProvided)
        )
    }

    @EventListener
    fun handleSurvivorLostLifePoints(event: SurvivorLostLifePoints) {
        val survivorDied = AtomicBoolean(false)
        actors.update(event.survivorId) {
            val survivor = it as Survivor
            val remainingLifePoints = survivor.lifePoints - event.damage
            if (remainingLifePoints <= 0) {
                survivorDied.set(true)
            }
            // TODO: change survivor instanciation for a builder that initiates all values from actual survivor
            Survivor(
                it.id,
                remainingLifePoints,
                survivor.name,
                survivor.weapon,
                survivor.experience,
                survivor.actionsCount,
                survivor.tokens
            )
        }
        if (survivorDied.get()) {
            eventsPublisher.fire(SurvivorDied(event.turn, event.survivorId))
        }
    }

    @EventListener
    fun handleSurvivorTokenRemoved(event: SurvivorTokenRemoved) {
        actors.update(event.survivorId) {
            val survivor = it as Survivor
            val updatedTokens = survivor.tokens.minus(event.token).toSet()

            Survivor(
                it.id,
                survivor.lifePoints,
                survivor.name,
                survivor.weapon,
                survivor.experience,
                survivor.actionsCount,
                updatedTokens
            )
        }
    }

    @EventListener
    fun handleSurvivorTokenAdded(event: SurvivorTokenAdded) {
        actors.update(event.survivorId) {
            val survivor = it as Survivor
            val updatedTokens = survivor.tokens.plus(event.token).toSet()

            Survivor(
                it.id,
                survivor.lifePoints,
                survivor.name,
                survivor.weapon,
                survivor.experience,
                survivor.actionsCount,
                updatedTokens
            )
        }
    }

    override fun clear() {
        history.clear()
    }

    override fun findActorBy(id: ActorId): Actor {
        return actors.findById(id)!!
    }

    override fun findLivingSurvivorBy(id: ActorId): Survivor? {
        val actor = actors.findById(id)

        if (actor != null && actor is Survivor && actor.lifeStatus === LifeStatus.ALIVE) {
            return actor;
        }
        return null
    }

    override fun allLivingSurvivors(): List<Survivor> {
        return actors.all()
            .filterIsInstance<Survivor>()
            .filter { survivor -> survivor.lifeStatus === LifeStatus.ALIVE }
    }

    override fun findZombieBy(id: ActorId): Zombie? {
        val actor = actors.findById(id)

        if (actor != null && actor is Zombie) {
            return actor
        }

        return null
    }

    private fun findAllZombies(): List<Zombie> {
        return actors.all()
            .filterIsInstance<Zombie>()
    }

    override fun findAllZombiesOnSameZoneAsSurvivor(survivorId: ActorId): Set<Zombie> {
        val survivorZone = zonesQueries.findByActorId(survivorId)!!
        return zonesQueries.findActorIdsOn(survivorZone.position)
            .mapNotNull { id -> findZombieBy(id) }
            .toSet()
    }

    override fun getRemainingActionsCountForActor(actorId: ActorId, turn: Int): Int {
        val actionsCount = findActorBy(actorId).actionsCount
        if (zombieSpawnedDuringTurn(turn, actorId)) {
            return 0
        }
        val spentActionsCount = findActionEventForActorIdAndTurn(actorId, turn).size
        return actionsCount - spentActionsCount
    }

    private fun findActionEventForActorIdAndTurn(actorId: ActorId, turn: Int): List<ActionEvent> {
        return history
            .filterIsInstance<ActionEvent>()
            .filter { actionEvent -> actionEvent.actorId == actorId && actionEvent.turn == turn }
    }

    override fun findCurrentSurvivorIdForTurn(turn: Int): ActorId? {
        return history
            .filterIsInstance<SurvivorsTurnStarted>()
            .findLast { survivorsTurnStarted -> survivorsTurnStarted.turn == turn }
            ?.survivorId
    }

    private fun zombieSpawnedDuringTurn(turn: Int, actorId: ActorId): Boolean {
        return history
            .filterIsInstance<ZombieSpawned>()
            .any { event -> event.turn == turn && event.zombie.id == actorId }
    }

    override fun findZombieIdsWithRemainingActions(turn: Int): List<ActorId> {
        return findAllZombies()
            .filter { zombie -> !zombieSpawnedDuringTurn(turn, zombie.id) }
            .filter { zombie -> findActionEventForActorIdAndTurn(zombie.id, turn).size < zombie.actionsCount }
            .map(Actor::id)
    }

    override fun findAttackingZombies(): List<Zombie> {
        return findAllZombies()
            .filter { zombie ->
                val zombieZone: Zone = zonesQueries.findByActorId(zombie.id)!!
                zonesQueries.findActorIdsOn(zombieZone.position)
                    .mapNotNull { id: ActorId -> findLivingSurvivorBy(id) }
                    .any()
            }
    }

    override fun getCurrentDangerLevel(): DangerLevel {
        return allLivingSurvivors()
            .map(Survivor::dangerLevel)
            .maxWith(DangerLevel.DANGER_LEVEL_COMPARATOR)
    }
}

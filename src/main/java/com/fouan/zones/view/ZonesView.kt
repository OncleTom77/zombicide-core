package com.fouan.zones.view

import com.fouan.actors.ActorId
import com.fouan.events.*
import com.fouan.zones.Direction
import com.fouan.zones.Position
import com.fouan.zones.Zone
import com.fouan.zones.Zone.ZoneMarker
import com.fouan.zones.view.ComputedZones.ComputedZone
import org.springframework.context.event.EventListener
import javax.inject.Named

@Named
class ZonesView : ZonesCommands, ZonesQueries {
    private val history: ArrayDeque<ZoneEvent> = ArrayDeque()
    private val zones = ComputedZones()
    private val connections = ComputedConnections()
    private val survivorIds: MutableList<ActorId> = mutableListOf()

    @EventListener
    fun handleZoneEvent(event: ZoneEvent) {
        history.addLast(event)
    }

    @EventListener
    fun handleBoardInitialized(event: BoardInitialized) {
        for (zone in event.zones) {
            zones.add(ComputedZone(zone))
        }
        event.connections
            .forEach { connections.add(it) }
    }

    @EventListener
    fun handleSurvivorAdded(event: SurvivorAdded) {
        addActor(event.survivor.id, event.zone.position)
        survivorIds.add(event.survivor.id)
    }

    @EventListener
    fun handleZombieSpawned(event: ZombieSpawned) {
        addActor(event.zombie.id, event.zone.position)
    }

    @EventListener
    fun handleSurvivorDied(event: SurvivorDied) {
        removeActor(event.survivorId)
        survivorIds.remove(event.survivorId)
    }

    @EventListener
    fun handleZombieDied(event: ZombieDied) {
        removeActor(event.zombie.id)
    }

    private fun addActor(actorMovedId: ActorId, newPosition: Position) {
        zones.update(newPosition) { computedZone ->
            val actorIds = computedZone.actorIds.toMutableList()
            actorIds.add(actorMovedId)
            ComputedZone(computedZone.zone, actorIds)
        }
    }

    private fun removeActor(actorDiedId: ActorId) {
        val oldZone: ComputedZone = zones.findByActorId(actorDiedId)!!
        zones.update(oldZone.position) { computedZone ->
            val actorIds = computedZone.actorIds
                .filter { it != actorDiedId }
            ComputedZone(computedZone.zone, actorIds)
        }
    }

    @EventListener
    fun handleSurvivorMoved(event: SurvivorMoved) {
        moveActor(event.actorId, event.position)
    }

    @EventListener
    fun handleZombieMoved(event: ZombieMoved) {
        moveActor(event.actorId, event.position)
    }

    private fun moveActor(actorMovedId: ActorId, newPosition: Position) {
        removeActor(actorMovedId)
        addActor(actorMovedId, newPosition)
    }

    override fun clear() {
        history.clear()
    }

    override fun findAll(): List<Zone> {
        return zones.all()
            .map(ComputedZone::zone)
    }

    override fun findAllConnections(): Set<Connection> {
        return connections.all()
    }

    override fun findByActorId(actorId: ActorId): Zone? {
        return zones.all()
            .find { it.actorIds.contains(actorId) }
            ?.zone
    }

    override fun findByMarker(marker: ZoneMarker): List<Zone> {
        return zones.all()
            .map(ComputedZone::zone)
            .filter { it.markers.contains(marker) }
    }

    override fun findActorIdsOn(position: Position): Set<ActorId> {
        return zones.all()
            .filter { it.zone.position == position }
            .flatMap(ComputedZone::actorIds)
            .toSet()
    }

    override fun findConnectedZones(zone: Zone): List<Zone> {
        return connections.findByZone(zone)
            .flatMap { connection -> listOf(connection.start, connection.end) }
            .filter { it != zone }
            .distinct()
    }

    override fun findNoisiestZones(withSurvivors: Boolean): List<Zone> {
        return findNoisiestZones(zones.all(), withSurvivors)
    }

    private fun findNoisiestZones(zones: Collection<ComputedZone>, withSurvivors: Boolean): List<Zone> {
        val zoneWithNoises = zones
            .map { computedZone ->
                val survivorIdsOnZone = computedZone.actorIds
                    .filter { actorId -> survivorIds.contains(actorId) }
                val noise = computedZone.noiseTokens + survivorIdsOnZone.size
                ZoneWithNoise(computedZone.zone, survivorIdsOnZone.isNotEmpty(), noise)
            }
            .filter { !withSurvivors || it.containsSurvivors }

        if (zoneWithNoises.isEmpty()) {
            return emptyList()
        }
        val maxNoise = zoneWithNoises.maxOf(ZoneWithNoise::noise)
        return zoneWithNoises
            .filter { it.noise == maxNoise }
            .map(ZoneWithNoise::zone)
    }

    override fun findNoisiestZonesInSight(zone: Zone): List<Zone> {
        val zonesInSight = findZonesInSight(zone)
            .map { zones.findByPosition(it.position)!! }
        return findNoisiestZones(zonesInSight, true)
    }

    private fun findZonesInSight(zone: Zone): List<Zone> {
        return Direction.values()
            .asList()
            .flatMap { direction -> getAllZonesInDirection(zone, direction, mutableListOf()) }
    }

    private fun getAllZonesInDirection(zone: Zone, direction: Direction, acc: MutableList<Zone>): List<Zone> {
        val nextPosition = direction.apply(zone.position)
        connections.findByZone(zone)
            .map { if (it.end == zone) it.start else it.end }
            .find { it.position == nextPosition }
            ?.let {
                acc.add(it)
                getAllZonesInDirection(it, direction, acc)
            }

        return acc
    }

    internal data class ZoneWithNoise(val zone: Zone, val containsSurvivors: Boolean, val noise: Int)
}

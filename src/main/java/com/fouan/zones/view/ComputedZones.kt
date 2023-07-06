package com.fouan.zones.view

import com.fouan.actors.ActorId
import com.fouan.zones.Position
import com.fouan.zones.Zone
import java.util.function.UnaryOperator

class ComputedZones {
    private val zones: MutableMap<Position, ComputedZone> = mutableMapOf()

    fun all(): Collection<ComputedZone> {
        return zones.values
    }

    fun add(zone: ComputedZone) {
        check(!zones.containsKey(zone.position)) { "Zone already exists" }
        zones[zone.position] = zone
    }

    fun update(position: Position, updater: UnaryOperator<ComputedZone>) {
        findByPosition(position)
            ?.let { zones[position] = updater.apply(it) }
    }

    fun findByPosition(position: Position): ComputedZone? {
        return zones[position]
    }

    fun findByActorId(actorId: ActorId): ComputedZone? {
        return zones.values
            .find { it.actorIds.contains(actorId) }
    }

    class ComputedZone(
        val zone: Zone,
        val actorIds: List<ActorId> = emptyList()
    ) {
        val noiseTokens = 0
        val position: Position
            get() = zone.position
    }
}

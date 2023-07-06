package com.fouan.zones.view

import com.fouan.zones.Zone

class ComputedConnections {
    private val connections: MutableSet<Connection> = mutableSetOf()

    fun add(connection: Connection) {
        connections.add(connection)
    }

    fun findByZone(zone: Zone): Set<Connection> {
        return connections.filter { it.implies(zone) }
            .toSet()
    }

    fun all(): Set<Connection> {
        return connections
    }
}

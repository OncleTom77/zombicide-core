package com.fouan.algorithm.pathfinding

import com.fouan.actors.ActorId
import com.fouan.zones.Direction
import com.fouan.zones.Position
import com.fouan.zones.Zone
import com.fouan.zones.view.Connection
import com.fouan.zones.view.ZonesQueries
import java.util.logging.Level
import java.util.logging.Logger

internal object ZombicidePathFinderTest {

    class StaticZonesQueries(private val zones: List<Zone>) : ZonesQueries {
        private val connections: Set<Connection>
        init {
            connections = mutableSetOf()
            zones.forEach { zone ->
                Direction.entries.forEach { direction ->
                    val nextPosition = direction.apply(zone.position)
                    zones.find { neighborZone -> neighborZone.position == nextPosition }
                        ?.let { neighborZone -> connections.add(Connection(zone, neighborZone)) }
                }
            }
        }

        override fun findAll(): List<Zone> {
            return zones
        }

        override fun findAllConnections(): Set<Connection> {
            return connections
        }

        override fun findConnectedZones(zone: Zone): List<Zone> {
            return connections.filter { it.implies(zone) }
                .flatMap { connection -> listOf(connection.start, connection.end) }
                .filter { it != zone }
                .distinct()
        }

        override fun findByActorId(actorId: ActorId): Zone? {
            throw NotImplementedError()
        }

        override fun findByMarker(marker: Zone.ZoneMarker): List<Zone> {
            throw NotImplementedError()
        }

        override fun findActorIdsOn(position: Position): Set<ActorId> {
            throw NotImplementedError()
        }

        override fun findNoisiestZones(withSurvivors: Boolean): List<Zone> {
            throw NotImplementedError()
        }

        override fun findNoisiestZonesInSight(zone: Zone): List<Zone> {
            throw NotImplementedError()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val zones = MutableList(9 * 6) { index: Int ->
            val position = Position(index % 9, index / 9)
            Zone.normalZone(position)
        }
        val from: Zone = zones.find { it.position.x == 0 && it.position.y == 0 }!!
        val to: Zone = zones.find { it.position.x == 8 && it.position.y == 5 }!!

        setLogLevel(Level.FINE)

        val result = ZombicidePathFinder(Dijkstra(), StaticZonesQueries(zones)).findNextZoneOfAllShortestPaths(from, to)
        println("Simple algo: $result")
    }

    private fun setLogLevel(logLevel: Level) {
        val root = Logger.getLogger("")
        root.level = logLevel
        for (handler in root.handlers) {
            handler.level = logLevel
        }
    }
}

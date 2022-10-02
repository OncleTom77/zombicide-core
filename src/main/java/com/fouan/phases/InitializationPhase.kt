package com.fouan.phases

import com.fouan.actors.ActorId
import com.fouan.actors.Survivor
import com.fouan.actors.zombies.Walker
import com.fouan.events.BoardInitialized
import com.fouan.events.SurvivorAdded
import com.fouan.events.ZombieSpawned
import com.fouan.game.view.GameView
import com.fouan.old.game.Direction
import com.fouan.weapons.Axe
import com.fouan.zones.Position
import com.fouan.zones.Zone
import com.fouan.zones.view.Connection
import com.fouan.zones.view.ZonesView
import java.util.*
import javax.inject.Named

@Named("initializationPhase")
internal class InitializationPhase(
    private val gameView: GameView,
    private val zonesView: ZonesView
) : Phase {

    private val width: Int = 9
    private val height: Int = 6

    override fun play() {
        initializeZones()
        initializeSurvivors()
        initializeZombies()
    }

    private fun initializeZones() {
        val zones = MutableList(width * height) { index: Int ->
            val position = Position(index % width, index / width)
            when (position) {
                Position(0, 0) -> Zone.startingZone(position)
                Position(width - 1, height - 1) -> Zone.exitZone(position)
                Position(1, 0) -> Zone.zombieSpawnZone(position)
                else -> Zone.normalZone(position)
            }
        }

        val connections = mutableSetOf<Connection>()
        zones.forEach { zone ->
            Direction.values().forEach { direction ->
                val nextPosition = direction.apply(zone.position)
                zones.find { neighborZone -> neighborZone.position.equals(nextPosition) }
                    ?.let { neighborZone -> connections.add(Connection(zone, neighborZone)) }
            }
        }

        gameView.fireEvent(BoardInitialized(gameView.currentTurn, zones, connections))
    }

    private fun initializeSurvivors() {
        val asim = Survivor(ActorId("Asim"), 3, "Asim", Axe(), 0)
        val berin = Survivor(ActorId("Berin"), 3, "Berin", Axe(), 0)

        val startingZone = zonesView.findByMarker(Zone.ZoneMarker.STARTING_ZONE)[0]
        gameView.fireEvent(SurvivorAdded(gameView.currentTurn, asim, startingZone))
        gameView.fireEvent(SurvivorAdded(gameView.currentTurn, berin, startingZone))
    }

    private fun initializeZombies() {
        val zombieSpawnZone = zonesView.findByMarker(Zone.ZoneMarker.ZOMBIE_SPAWN)[0]
        gameView.fireEvent(ZombieSpawned(gameView.currentTurn, Walker(ActorId("1")), zombieSpawnZone))
        gameView.fireEvent(ZombieSpawned(gameView.currentTurn, Walker(ActorId("2")), zombieSpawnZone))
    }
}
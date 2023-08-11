package com.fouan.phases

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import com.fouan.actors.ActorId
import com.fouan.actors.view.ActorsQueries
import com.fouan.actors.zombies.Fatty
import com.fouan.actors.zombies.Walker
import com.fouan.algorithm.pathfinding.PathFinder
import com.fouan.events.Event
import com.fouan.events.ZombieMoved
import com.fouan.events.ZombieSpawned
import com.fouan.game.view.GameView
import com.fouan.zones.Position
import com.fouan.zones.Zone.Companion.normalZone
import com.fouan.zones.view.ZonesQueries
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ZombiesPhaseTest {

    @InjectMockKs
    private lateinit var zombiePhase: ZombiesPhase

    @MockK(relaxed = true)
    private lateinit var gameView: GameView

    @MockK(relaxed = true)
    private lateinit var actorsQueries: ActorsQueries

    @MockK(relaxed = true)
    private lateinit var zonesQueries: ZonesQueries

    @MockK(relaxed = true)
    private lateinit var pathFinder: PathFinder

    private var capturedEvents: MutableList<Event<*>> = mutableListOf()

    val currentTurn = 1

    @BeforeEach
    fun setUp() {
        capturedEvents.clear()
        every { gameView.currentTurn } returns currentTurn
        every { gameView.fireEvent(capture(capturedEvents)) } just Runs
        ActorId.resetId()
    }

    @Test
    fun splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_one_zombie() {
        val walker = Walker(ActorId())
        val zone = normalZone(Position(0, 1))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker),
            setOf(zone)
        )

        verify(exactly = 1) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_multiple_zombies_of_same_type() {
        val walker = Walker(ActorId())
        val walker2 = Walker(ActorId())
        val zone = normalZone(Position(0, 1))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, walker2),
            setOf(zone)
        )

        verify(exactly = 2) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieMoved(currentTurn, walker2.id, zone.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_one_zombie_for_different_type() {
        val walker = Walker(ActorId())
        val fatty = Fatty(ActorId())
        val zone = normalZone(Position(0, 1))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, fatty),
            setOf(zone)
        )

        verify(exactly = 2) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieMoved(currentTurn, fatty.id, zone.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_multiple_zombies_for_different_type() {
        val walker = Walker(ActorId())
        val walker2 = Walker(ActorId())
        val fatty = Fatty(ActorId())
        val fatty2 = Fatty(ActorId())
        val fatty3 = Fatty(ActorId())
        val zone = normalZone(Position(0, 1))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, walker2, fatty, fatty2, fatty3),
            setOf(zone)
        )

        verify(exactly = 5) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieMoved(currentTurn, walker2.id, zone.position),
            ZombieMoved(currentTurn, fatty.id, zone.position),
            ZombieMoved(currentTurn, fatty2.id, zone.position),
            ZombieMoved(currentTurn, fatty3.id, zone.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_split_zombies_when_there_are_as_many_zombies_as_destination_zones_with_one_zombie_type() {
        val walker = Walker(ActorId())
        val walker2 = Walker(ActorId())
        val zone = normalZone(Position(0, 1))
        val zone2 = normalZone(Position(1, 0))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, walker2),
            setOf(zone, zone2)
        )

        verify(exactly = 2) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieMoved(currentTurn, walker2.id, zone2.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_split_zombies_when_there_are_as_many_zombies_as_destination_zones_with_different_zombie_type() {
        val walker = Walker(ActorId())
        val walker2 = Walker(ActorId())
        val fatty = Fatty(ActorId())
        val fatty2 = Fatty(ActorId())
        val zone = normalZone(Position(0, 1))
        val zone2 = normalZone(Position(1, 0))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, walker2, fatty, fatty2),
            setOf(zone, zone2)
        )

        verify(exactly = 4) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieMoved(currentTurn, walker2.id, zone2.position),
            ZombieMoved(currentTurn, fatty.id, zone.position),
            ZombieMoved(currentTurn, fatty2.id, zone2.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_split_zombies_when_there_are_2_zombies_per_type_per_destination_zone_with_one_zombie_type() {
        val walker = Walker(ActorId())
        val walker2 = Walker(ActorId())
        val walker3 = Walker(ActorId())
        val walker4 = Walker(ActorId())
        val zone = normalZone(Position(0, 1))
        val zone2 = normalZone(Position(1, 0))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, walker2, walker3, walker4),
            setOf(zone, zone2)
        )

        verify(exactly = 4) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieMoved(currentTurn, walker2.id, zone2.position),
            ZombieMoved(currentTurn, walker3.id, zone.position),
            ZombieMoved(currentTurn, walker4.id, zone2.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_split_zombies_when_there_are_2_zombies_per_type_per_destination_zone_with_different_zombie_type() {
        val walker = Walker(ActorId())
        val walker2 = Walker(ActorId())
        val walker3 = Walker(ActorId())
        val walker4 = Walker(ActorId())
        val fatty = Fatty(ActorId())
        val fatty2 = Fatty(ActorId())
        val fatty3 = Fatty(ActorId())
        val fatty4 = Fatty(ActorId())
        val zone = normalZone(Position(0, 1))
        val zone2 = normalZone(Position(1, 0))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, walker2, walker3, walker4, fatty, fatty2, fatty3, fatty4),
            setOf(zone, zone2)
        )

        verify(exactly = 8) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieMoved(currentTurn, walker2.id, zone2.position),
            ZombieMoved(currentTurn, walker3.id, zone.position),
            ZombieMoved(currentTurn, walker4.id, zone2.position),
            ZombieMoved(currentTurn, fatty.id, zone.position),
            ZombieMoved(currentTurn, fatty2.id, zone2.position),
            ZombieMoved(currentTurn, fatty3.id, zone.position),
            ZombieMoved(currentTurn, fatty4.id, zone2.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_one_zombie() {
        val walker = Walker(ActorId())
        val zone = normalZone(Position(0, 1))
        val zone2 = normalZone(Position(1, 0))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker),
            setOf(zone, zone2)
        )

        verify(exactly = 2) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieSpawned(currentTurn, Walker(ActorId("1")), zone2.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_one_zombie_for_different_type() {
        val walker = Walker(ActorId())
        val fatty = Fatty(ActorId())
        val zone = normalZone(Position(0, 1))
        val zone2 = normalZone(Position(1, 0))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, fatty),
            setOf(zone, zone2)
        )

        verify(exactly = 4) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieSpawned(currentTurn, Walker(ActorId("2")), zone2.position),
            ZombieMoved(currentTurn, fatty.id, zone.position),
            ZombieSpawned(currentTurn, Fatty(ActorId("3")), zone2.position),
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_not_enough_zombie_for_only_one_type() {
        val walker = Walker(ActorId())
        val walker2 = Walker(ActorId())
        val fatty = Fatty(ActorId())
        val zone = normalZone(Position(0, 1))
        val zone2 = normalZone(Position(1, 0))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, walker2, fatty),
            setOf(zone, zone2)
        )

        verify(exactly = 4) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieMoved(currentTurn, walker2.id, zone2.position),
            ZombieMoved(currentTurn, fatty.id, zone.position),
            ZombieSpawned(currentTurn, Fatty(ActorId("3")), zone2.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_multiple_zombies_of_one_type_but_not_enough_for_equal_group() {
        val walker = Walker(ActorId())
        val walker2 = Walker(ActorId())
        val walker3 = Walker(ActorId())
        val zone = normalZone(Position(0, 1))
        val zone2 = normalZone(Position(1, 0))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, walker2, walker3),
            setOf(zone, zone2)
        )

        verify(exactly = 4) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieMoved(currentTurn, walker2.id, zone2.position),
            ZombieMoved(currentTurn, walker3.id, zone.position),
            ZombieSpawned(currentTurn, Walker(ActorId("3")), zone2.position)
        )
    }

    @Test
    fun splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_multiple_zombies_of_different_type_but_not_enough_for_equal_group() {
        val walker = Walker(ActorId())
        val walker2 = Walker(ActorId())
        val walker3 = Walker(ActorId())
        val fatty = Fatty(ActorId())
        val fatty2 = Fatty(ActorId())
        val fatty3 = Fatty(ActorId())
        val zone = normalZone(Position(0, 1))
        val zone2 = normalZone(Position(1, 0))

        zombiePhase.splitZombiesInEqualGroups(
            listOf(walker, walker2, walker3, fatty, fatty2, fatty3),
            setOf(zone, zone2)
        )

        verify(exactly = 8) { gameView.fireEvent(any()) }
        assertThat(capturedEvents).containsExactlyInAnyOrder(
            ZombieMoved(currentTurn, walker.id, zone.position),
            ZombieMoved(currentTurn, walker2.id, zone2.position),
            ZombieMoved(currentTurn, walker3.id, zone.position),
            ZombieSpawned(currentTurn, Walker(ActorId("6")), zone2.position),
            ZombieMoved(currentTurn, fatty.id, zone.position),
            ZombieMoved(currentTurn, fatty2.id, zone2.position),
            ZombieMoved(currentTurn, fatty3.id, zone.position),
            ZombieSpawned(currentTurn, Fatty(ActorId("7")), zone2.position),
        )
    }
}
package com.fouan.phases;

import com.fouan.actors.ActorId;
import com.fouan.actors.view.ActorsQueries;
import com.fouan.actors.zombies.Fatty;
import com.fouan.actors.zombies.Walker;
import com.fouan.actors.zombies.Zombie;
import com.fouan.algorithm.pathfinding.PathFinder;
import com.fouan.events.Event;
import com.fouan.events.ZombieMoved;
import com.fouan.game.view.GameView;
import com.fouan.zones.Position;
import com.fouan.zones.Zone;
import com.fouan.zones.view.ZonesQueries;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZombiePhaseTest {

    @InjectMocks
    private ZombiesPhase zombiePhase;
    @Mock
    private GameView gameView;
    @Mock
    private ActorsQueries actorsQueries;
    @Mock
    private ZonesQueries zonesQueries;
    @Mock
    private PathFinder pathFinder;

    @Captor
    private ArgumentCaptor<Event<?>> eventArgumentCaptor;

    public final int currentTurn = 1;

    @BeforeEach
    void setUp() {
        when(gameView.getCurrentTurn()).thenReturn(currentTurn);
    }

    @Test
    void splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_one_zombie() {
        Zombie walker = new Walker(new ActorId());
        Zone zone = Zone.Companion.normalZone(new Position(0, 1));

        zombiePhase.splitZombiesInEqualGroups(List.of(walker), Set.of(zone));

        verify(gameView).fireEvent(eventArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getAllValues()).containsExactlyInAnyOrder(
                new ZombieMoved(currentTurn, walker.getId(), zone.getPosition())
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_multiple_zombies_of_same_type() {
        Zombie walker = new Walker(new ActorId());
        Zombie walker2 = new Walker(new ActorId());
        Zone zone = Zone.Companion.normalZone(new Position(0, 1));

        zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2), Set.of(zone));

        verify(gameView, times(2)).fireEvent(eventArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getAllValues()).containsExactlyInAnyOrder(
                new ZombieMoved(currentTurn, walker.getId(), zone.getPosition()),
                new ZombieMoved(currentTurn, walker2.getId(), zone.getPosition())
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_one_zombie_for_different_type() {
        Zombie walker = new Walker(new ActorId());
        Zombie fatty = new Fatty(new ActorId());
        Zone zone = Zone.Companion.normalZone(new Position(0, 1));

        zombiePhase.splitZombiesInEqualGroups(List.of(walker, fatty), Set.of(zone));

        verify(gameView, times(2)).fireEvent(eventArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getAllValues()).containsExactlyInAnyOrder(
                new ZombieMoved(currentTurn, walker.getId(), zone.getPosition()),
                new ZombieMoved(currentTurn, fatty.getId(), zone.getPosition())
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_multiple_zombies_for_different_type() {
        Zombie walker = new Walker(new ActorId());
        Zombie walker2 = new Walker(new ActorId());
        Zombie fatty = new Fatty(new ActorId());
        Zombie fatty2 = new Fatty(new ActorId());
        Zombie fatty3 = new Fatty(new ActorId());
        Zone zone = Zone.Companion.normalZone(new Position(0, 1));

        zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, fatty, fatty2, fatty3), Set.of(zone));

        verify(gameView, times(5)).fireEvent(eventArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getAllValues()).containsExactlyInAnyOrder(
                new ZombieMoved(currentTurn, walker.getId(), zone.getPosition()),
                new ZombieMoved(currentTurn, walker2.getId(), zone.getPosition()),
                new ZombieMoved(currentTurn, fatty.getId(), zone.getPosition()),
                new ZombieMoved(currentTurn, fatty2.getId(), zone.getPosition()),
                new ZombieMoved(currentTurn, fatty3.getId(), zone.getPosition())
        );
    }

//    @Test
//    void splitZombiesInEqualGroups_should_split_zombies_when_there_are_as_many_zombies_as_destination_zones_with_one_zombie_type() {
//        Zombie walker = new Walker(new ActorId());
//        Zombie walker2 = new Walker(new ActorId());
//        Zone zone = Zone.Companion.normalZone(new Position(0, 1));
//        Zone zone2 = new Zone(new Position(1, 0));
//
//        zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2), Set.of(zone, zone2));
//
//        assertThat(commands).containsExactlyInAnyOrder(
//                new MoveCommand(walker, zone),
//                new MoveCommand(walker2, zone2)
//        );
//    }
//
//    @Test
//    void splitZombiesInEqualGroups_should_split_zombies_when_there_are_as_many_zombies_as_destination_zones_with_different_zombie_type() {
//        Zombie walker = new Walker(new ActorId());
//        Zombie walker2 = new Walker(new ActorId());
//        Zombie fatty = new Fatty(new ActorId());
//        Zombie fatty2 = new Fatty(new ActorId());
//        Zone zone = Zone.Companion.normalZone(new Position(0, 1));
//        Zone zone2 = new Zone(new Position(1, 0));
//
//        zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, fatty, fatty2), Set.of(zone, zone2));
//
//        assertThat(commands).containsExactlyInAnyOrder(
//                new MoveCommand(walker, zone),
//                new MoveCommand(walker2, zone2),
//                new MoveCommand(fatty, zone),
//                new MoveCommand(fatty2, zone2)
//        );
//    }
//
//    @Test
//    void splitZombiesInEqualGroups_should_split_zombies_when_there_are_2_zombies_per_type_per_destination_zone_with_one_zombie_type() {
//        Zombie walker = new Walker(new ActorId());
//        Zombie walker2 = new Walker(new ActorId());
//        Zombie walker3 = new Zombie(output, initialZone, ZombieType.WALKER);
//        Zombie walker4 = new Zombie(output, initialZone, ZombieType.WALKER);
//        Zone zone = Zone.Companion.normalZone(new Position(0, 1));
//        Zone zone2 = new Zone(new Position(1, 0));
//
//        zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, walker3, walker4), Set.of(zone, zone2));
//
//        assertThat(commands).containsExactlyInAnyOrder(
//                new MoveCommand(walker, zone),
//                new MoveCommand(walker2, zone2),
//                new MoveCommand(walker3, zone),
//                new MoveCommand(walker4, zone2)
//        );
//    }
//
//    @Test
//    void splitZombiesInEqualGroups_should_split_zombies_when_there_are_2_zombies_per_type_per_destination_zone_with_different_zombie_type() {
//        Zombie walker = new Walker(new ActorId());
//        Zombie walker2 = new Walker(new ActorId());
//        Zombie walker3 = new Zombie(output, initialZone, ZombieType.WALKER);
//        Zombie walker4 = new Zombie(output, initialZone, ZombieType.WALKER);
//        Zombie fatty = new Fatty(new ActorId());
//        Zombie fatty2 = new Fatty(new ActorId());
//        Zombie fatty3 = new Fatty(new ActorId());
//        Zombie fatty4 = new Fatty(new ActorId());
//        Zone zone = Zone.Companion.normalZone(new Position(0, 1));
//        Zone zone2 = new Zone(new Position(1, 0));
//
//        zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, walker3, walker4, fatty, fatty2, fatty3, fatty4), Set.of(zone, zone2));
//
//        assertThat(commands).containsExactlyInAnyOrder(
//                new MoveCommand(walker, zone),
//                new MoveCommand(walker2, zone2),
//                new MoveCommand(walker3, zone),
//                new MoveCommand(walker4, zone2),
//                new MoveCommand(fatty, zone),
//                new MoveCommand(fatty2, zone2),
//                new MoveCommand(fatty3, zone),
//                new MoveCommand(fatty4, zone2)
//        );
//    }
//
//    @Test
//    void splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_one_zombie() {
//        Zombie walker = new Walker(new ActorId());
//        Zone zone = Zone.Companion.normalZone(new Position(0, 1));
//        Zone zone2 = new Zone(new Position(1, 0));
//
//        zombiePhase.splitZombiesInEqualGroups(List.of(walker), Set.of(zone, zone2));
//
//        assertThat(commands).containsExactlyInAnyOrder(
//                new MoveCommand(walker, zone),
//                new GenerateZombieAfterSplitCommand(zone2, ZombieType.WALKER, actorFactory)
//        );
//    }
//
//    @Test
//    void splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_one_zombie_for_different_type() {
//        Zombie walker = new Walker(new ActorId());
//        Zombie fatty = new Fatty(new ActorId());
//        Zone zone = Zone.Companion.normalZone(new Position(0, 1));
//        Zone zone2 = new Zone(new Position(1, 0));
//
//        zombiePhase.splitZombiesInEqualGroups(List.of(walker, fatty), Set.of(zone, zone2));
//
//        assertThat(commands).containsExactlyInAnyOrder(
//                new MoveCommand(walker, zone),
//                new GenerateZombieAfterSplitCommand(zone2, ZombieType.WALKER, actorFactory),
//                new MoveCommand(fatty, zone),
//                new GenerateZombieAfterSplitCommand(zone2, ZombieType.FATTY, actorFactory)
//        );
//    }
//
//    @Test
//    void splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_not_enough_zombie_for_only_one_type() {
//        Zombie walker = new Walker(new ActorId());
//        Zombie walker2 = new Walker(new ActorId());
//        Zombie fatty = new Fatty(new ActorId());
//        Zone zone = Zone.Companion.normalZone(new Position(0, 1));
//        Zone zone2 = new Zone(new Position(1, 0));
//
//        zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, fatty), Set.of(zone, zone2));
//
//        assertThat(commands).containsExactlyInAnyOrder(
//                new MoveCommand(walker, zone),
//                new MoveCommand(walker2, zone2),
//                new MoveCommand(fatty, zone),
//                new GenerateZombieAfterSplitCommand(zone2, ZombieType.FATTY, actorFactory)
//        );
//    }
//
//    @Test
//    void splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_multiple_zombies_of_one_type_but_not_enough_for_equal_group() {
//        Zombie walker = new Walker(new ActorId());
//        Zombie walker2 = new Walker(new ActorId());
//        Zombie walker3 = new Zombie(output, initialZone, ZombieType.WALKER);
//        Zone zone = Zone.Companion.normalZone(new Position(0, 1));
//        Zone zone2 = new Zone(new Position(1, 0));
//
//        zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, walker3), Set.of(zone, zone2));
//
//        assertThat(commands).containsExactlyInAnyOrder(
//                new MoveCommand(walker, zone),
//                new MoveCommand(walker2, zone2),
//                new MoveCommand(walker3, zone),
//                new GenerateZombieAfterSplitCommand(zone2, ZombieType.WALKER, actorFactory)
//        );
//    }
//
//    @Test
//    void splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_multiple_zombies_of_different_type_but_not_enough_for_equal_group() {
//        Zombie walker = new Walker(new ActorId());
//        Zombie walker2 = new Walker(new ActorId());
//        Zombie walker3 = new Zombie(output, initialZone, ZombieType.WALKER);
//        Zombie fatty = new Fatty(new ActorId());
//        Zombie fatty2 = new Fatty(new ActorId());
//        Zombie fatty3 = new Fatty(new ActorId());
//        Zone zone = Zone.Companion.normalZone(new Position(0, 1));
//        Zone zone2 = new Zone(new Position(1, 0));
//
//        zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, walker3, fatty, fatty2, fatty3), Set.of(zone, zone2));
//
//        assertThat(commands).containsExactlyInAnyOrder(
//                new MoveCommand(walker, zone),
//                new MoveCommand(walker2, zone2),
//                new MoveCommand(walker3, zone),
//                new GenerateZombieAfterSplitCommand(zone2, ZombieType.WALKER, actorFactory),
//                new MoveCommand(fatty, zone),
//                new MoveCommand(fatty2, zone2),
//                new MoveCommand(fatty3, zone),
//                new GenerateZombieAfterSplitCommand(zone2, ZombieType.FATTY, actorFactory)
//        );
//    }

}
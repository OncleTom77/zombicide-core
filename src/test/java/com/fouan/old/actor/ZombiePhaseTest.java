package com.fouan.old.actor;

import com.fouan.zones.Position;
import com.fouan.old.board.Zone;
import com.fouan.old.command.Command;
import com.fouan.old.command.GenerateZombieAfterSplitCommand;
import com.fouan.old.command.MoveCommand;
import com.fouan.display.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ZombiePhaseTest {

    @InjectMocks
    private ZombiePhase zombiePhase;
    @Mock
    private Output output;
    @Mock
    private ActorFactory actorFactory;

    private Zone initialZone;

    @BeforeEach
    void setUp() {
        initialZone = new Zone(new Position(1, 1));
    }

    @Test
    void splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_one_zombie() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zone zone = new Zone(new Position(0, 1));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker), Set.of(zone));

        assertThat(commands).containsExactlyInAnyOrder(new MoveCommand(walker, zone));
    }

    @Test
    void splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_multiple_zombies_of_same_type() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker2 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zone zone = new Zone(new Position(0, 1));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2), Set.of(zone));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new MoveCommand(walker2, zone)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_one_zombie_for_different_type() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie fatty = new Zombie(output, initialZone, ZombieType.FATTY);
        Zone zone = new Zone(new Position(0, 1));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, fatty), Set.of(zone));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new MoveCommand(fatty, zone)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_not_split_zombies_when_there_is_only_one_destination_zone_and_multiple_zombies_for_different_type() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker2 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie fatty = new Zombie(output, initialZone, ZombieType.FATTY);
        Zombie fatty2 = new Zombie(output, initialZone, ZombieType.FATTY);
        Zombie fatty3 = new Zombie(output, initialZone, ZombieType.FATTY);
        Zone zone = new Zone(new Position(0, 1));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, fatty, fatty2, fatty3), Set.of(zone));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new MoveCommand(walker2, zone),
                new MoveCommand(fatty, zone),
                new MoveCommand(fatty2, zone),
                new MoveCommand(fatty3, zone)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_split_zombies_when_there_are_as_many_zombies_as_destination_zones_with_one_zombie_type() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker2 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zone zone = new Zone(new Position(0, 1));
        Zone zone2 = new Zone(new Position(1, 0));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2), Set.of(zone, zone2));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new MoveCommand(walker2, zone2)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_split_zombies_when_there_are_as_many_zombies_as_destination_zones_with_different_zombie_type() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker2 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie fatty = new Zombie(output, initialZone, ZombieType.FATTY);
        Zombie fatty2 = new Zombie(output, initialZone, ZombieType.FATTY);
        Zone zone = new Zone(new Position(0, 1));
        Zone zone2 = new Zone(new Position(1, 0));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, fatty, fatty2), Set.of(zone, zone2));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new MoveCommand(walker2, zone2),
                new MoveCommand(fatty, zone),
                new MoveCommand(fatty2, zone2)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_split_zombies_when_there_are_2_zombies_per_type_per_destination_zone_with_one_zombie_type() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker2 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker3 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker4 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zone zone = new Zone(new Position(0, 1));
        Zone zone2 = new Zone(new Position(1, 0));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, walker3, walker4), Set.of(zone, zone2));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new MoveCommand(walker2, zone2),
                new MoveCommand(walker3, zone),
                new MoveCommand(walker4, zone2)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_split_zombies_when_there_are_2_zombies_per_type_per_destination_zone_with_different_zombie_type() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker2 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker3 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker4 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie fatty = new Zombie(output, initialZone, ZombieType.FATTY);
        Zombie fatty2 = new Zombie(output, initialZone, ZombieType.FATTY);
        Zombie fatty3 = new Zombie(output, initialZone, ZombieType.FATTY);
        Zombie fatty4 = new Zombie(output, initialZone, ZombieType.FATTY);
        Zone zone = new Zone(new Position(0, 1));
        Zone zone2 = new Zone(new Position(1, 0));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, walker3, walker4, fatty, fatty2, fatty3, fatty4), Set.of(zone, zone2));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new MoveCommand(walker2, zone2),
                new MoveCommand(walker3, zone),
                new MoveCommand(walker4, zone2),
                new MoveCommand(fatty, zone),
                new MoveCommand(fatty2, zone2),
                new MoveCommand(fatty3, zone),
                new MoveCommand(fatty4, zone2)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_one_zombie() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zone zone = new Zone(new Position(0, 1));
        Zone zone2 = new Zone(new Position(1, 0));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker), Set.of(zone, zone2));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new GenerateZombieAfterSplitCommand(zone2, ZombieType.WALKER, actorFactory)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_one_zombie_for_different_type() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie fatty = new Zombie(output, initialZone, ZombieType.FATTY);
        Zone zone = new Zone(new Position(0, 1));
        Zone zone2 = new Zone(new Position(1, 0));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, fatty), Set.of(zone, zone2));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new GenerateZombieAfterSplitCommand(zone2, ZombieType.WALKER, actorFactory),
                new MoveCommand(fatty, zone),
                new GenerateZombieAfterSplitCommand(zone2, ZombieType.FATTY, actorFactory)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_not_enough_zombie_for_only_one_type() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker2 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie fatty = new Zombie(output, initialZone, ZombieType.FATTY);
        Zone zone = new Zone(new Position(0, 1));
        Zone zone2 = new Zone(new Position(1, 0));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, fatty), Set.of(zone, zone2));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new MoveCommand(walker2, zone2),
                new MoveCommand(fatty, zone),
                new GenerateZombieAfterSplitCommand(zone2, ZombieType.FATTY, actorFactory)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_multiple_zombies_of_one_type_but_not_enough_for_equal_group() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker2 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker3 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zone zone = new Zone(new Position(0, 1));
        Zone zone2 = new Zone(new Position(1, 0));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, walker3), Set.of(zone, zone2));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new MoveCommand(walker2, zone2),
                new MoveCommand(walker3, zone),
                new GenerateZombieAfterSplitCommand(zone2, ZombieType.WALKER, actorFactory)
        );
    }

    @Test
    void splitZombiesInEqualGroups_should_generate_zombies_when_there_are_multiple_destination_zones_and_multiple_zombies_of_different_type_but_not_enough_for_equal_group() {
        Zombie walker = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker2 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie walker3 = new Zombie(output, initialZone, ZombieType.WALKER);
        Zombie fatty = new Zombie(output, initialZone, ZombieType.FATTY);
        Zombie fatty2 = new Zombie(output, initialZone, ZombieType.FATTY);
        Zombie fatty3 = new Zombie(output, initialZone, ZombieType.FATTY);
        Zone zone = new Zone(new Position(0, 1));
        Zone zone2 = new Zone(new Position(1, 0));

        List<Command> commands = zombiePhase.splitZombiesInEqualGroups(List.of(walker, walker2, walker3, fatty, fatty2, fatty3), Set.of(zone, zone2));

        assertThat(commands).containsExactlyInAnyOrder(
                new MoveCommand(walker, zone),
                new MoveCommand(walker2, zone2),
                new MoveCommand(walker3, zone),
                new GenerateZombieAfterSplitCommand(zone2, ZombieType.WALKER, actorFactory),
                new MoveCommand(fatty, zone),
                new MoveCommand(fatty2, zone2),
                new MoveCommand(fatty3, zone),
                new GenerateZombieAfterSplitCommand(zone2, ZombieType.FATTY, actorFactory)
        );
    }

}
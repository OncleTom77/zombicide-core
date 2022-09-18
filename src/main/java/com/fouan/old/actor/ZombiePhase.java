package com.fouan.old.actor;

import com.fouan.algorithm.pathfinding.ZombicidePathFinder;
import com.fouan.old.board.ZoneUtils;
import com.fouan.old.board.Zone;
import com.fouan.old.board.Zones;
import com.fouan.old.command.Command;
import com.fouan.old.command.GenerateZombieAfterSplitCommand;
import com.fouan.old.command.MoveCommand;
import com.fouan.old.command.ZombieAttackCommand;
import com.fouan.old.game.ActorSelection;
import com.fouan.old.io.ChoiceMaker;
import com.fouan.display.Output;
import com.fouan.old.utils.ListUtils;

import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Named
public class ZombiePhase {

    private final ChoiceMaker choiceMaker;
    private final Output output;
    private final ActorSelection actorSelection;
    private final ActorFactory actorFactory;
    private final ZombicidePathFinder zombicidePathFinder;

    public ZombiePhase(ChoiceMaker choiceMaker, Output output, ActorSelection actorSelection, ActorFactory actorFactory, ZombicidePathFinder zombicidePathFinder) {
        this.choiceMaker = choiceMaker;
        this.output = output;
        this.actorSelection = actorSelection;
        this.actorFactory = actorFactory;
        this.zombicidePathFinder = zombicidePathFinder;
    }

    public List<Command> handleZombieAttacks(List<Zombie> attackingZombies, List<Survivor> survivors) {
        output.display(attackingZombies.size() + " zombies attack");
        List<Integer> totalWounds = attackingZombies.stream()
                .map(zombie -> zombie.getType().getDamage())
                .toList();

        Map<Survivor, Integer> woundsPerSurvivor = getWoundsPerSurvivor(survivors, totalWounds);

        return woundsPerSurvivor.entrySet()
                .stream()
                .map(entry -> new ZombieAttackCommand(entry.getKey(), entry.getValue()))
                .map(zombieAttackCommand -> (Command) zombieAttackCommand)
                .toList();
    }

    private Map<Survivor, Integer> getWoundsPerSurvivor(List<Survivor> survivors, List<Integer> wounds) {
        Map<Survivor, Integer> woundsPerSurvivors = new HashMap<>();

        if (survivors.size() == 1) {
            int totalWounds = wounds.stream()
                    .mapToInt(value -> value)
                    .sum();
            woundsPerSurvivors.put(survivors.get(0), totalWounds);
        } else {
            List<Integer> remainingWounds = new ArrayList<>(wounds);
            do {
                output.display("Remaining wounds to allocate: " + remainingWounds);
                Survivor survivor = actorSelection.chooseActor(survivors);

                output.display("Allocate wounds for " + survivor + " (already allocated: " + woundsPerSurvivors.getOrDefault(survivor, 0) + "):");
                Set<Integer> choices = Stream.concat(Stream.of(0), remainingWounds.stream())
                        .collect(Collectors.toSet());
                int choice = choiceMaker.getChoice(choices);

                if (choice > 0) {
                    remainingWounds.remove((Object) choice);
                    woundsPerSurvivors.merge(survivor, choice, Integer::sum);
                }
            } while (!remainingWounds.isEmpty());
        }
        return woundsPerSurvivors;
    }

    public List<Command> handleZombieMove(Zones zones, Zone currentZone, List<Zombie> activatedZombies, List<Zone> defaultNoisiestZones) {
        List<Zombie> zombies = currentZone.getZombies()
                .stream()
                .filter(zombie -> !activatedZombies.contains(zombie))
                .toList();

        if (zombies.isEmpty()) {
            return Collections.emptyList();
        }
        activatedZombies.addAll(zombies);

        List<Zone> destinationZones = getDestinationZones(currentZone, defaultNoisiestZones);
        Set<Zone> nextZones = destinationZones.stream()
                .map(destination -> zombicidePathFinder.findNextZoneOfAllShortestPaths(zones, currentZone, destination))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        // TODO: create Group command to allow multiple commands to be handle together (applied or rollbacked)
        return splitZombiesInEqualGroups(zombies, nextZones);
    }

    private List<Zone> getDestinationZones(Zone zone, List<Zone> defaultNoisiestZones) {
        List<Zone> noisiestZonesInSight = ZoneUtils.getNoisiestZones(zone.getAllInSight(), true);

        return noisiestZonesInSight.isEmpty()
                ? defaultNoisiestZones
                : noisiestZonesInSight;
    }

    List<Command> splitZombiesInEqualGroups(List<Zombie> zombies, Set<Zone> nextZones) {
        Map<ZombieType, ArrayDeque<Zombie>> zombiesPerType = zombies.stream()
                .collect(toMap(
                        Zombie::getType,
                        o -> new ArrayDeque<>(List.of(o)),
                        (a, b) -> ListUtils.concat(a, b, ArrayDeque::new)
                ));

        List<Command> result = new ArrayList<>();

        zombiesPerType.forEach((zombieType, existingZombies) -> {
            while (!existingZombies.isEmpty()) {
                for (Zone nextZone : nextZones) {
                    if (existingZombies.isEmpty()) {
                        result.add(new GenerateZombieAfterSplitCommand(nextZone, zombieType, actorFactory));
                    } else {
                        result.add(new MoveCommand(existingZombies.pop(), nextZone));
                    }
                }
            }
        });
        return result;
    }
}

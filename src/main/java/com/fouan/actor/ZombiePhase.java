package com.fouan.actor;

import com.fouan.board.Zone;
import com.fouan.command.Command;
import com.fouan.command.MoveCommand;
import com.fouan.command.ZombieAttackCommand;
import com.fouan.game.ActorSelection;
import com.fouan.io.ChoiceMaker;
import com.fouan.io.Output;

import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named
public class ZombiePhase {

    private final ChoiceMaker choiceMaker;
    private final Output output;
    private final ActorSelection actorSelection;
    private final Random random;

    public ZombiePhase(ChoiceMaker choiceMaker, Output output, ActorSelection actorSelection, Random random) {
        this.choiceMaker = choiceMaker;
        this.output = output;
        this.actorSelection = actorSelection;
        this.random = random;
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

    public List<Command> handleZombieMove(Zone zone, List<Zombie> activatedZombies) {
        List<Zombie> zombies = zone.getZombies()
                .stream()
                .filter(zombie -> !activatedZombies.contains(zombie))
                .toList();
        activatedZombies.addAll(zombies);

        Zone destinationZone = getDestinationZone(zone);

        return zombies.stream()
                .map(zombie -> new MoveCommand(zombie, destinationZone))
                .map(moveCommand -> (Command) moveCommand)
                .toList();
    }

    public Zone getDestinationZone(Zone zone) {
        // TODO: make Zombies move together toward survivors
        List<Zone> possibleZones = zone.getConnectedZones();
        int randomZoneIndex = random.nextInt(possibleZones.size());
        return possibleZones.get(randomZoneIndex);
    }
}

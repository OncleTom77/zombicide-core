package com.fouan.actor;

import com.fouan.board.Zone;
import com.fouan.game.ActorSelection;
import com.fouan.io.ChoiceMaker;
import com.fouan.io.Output;

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

    public ZombiePhase(ChoiceMaker choiceMaker, Output output, ActorSelection actorSelection) {
        this.choiceMaker = choiceMaker;
        this.output = output;
        this.actorSelection = actorSelection;
    }

    /**
     * First, play zombies that can attack
     * Then, play zombies that must move
     */
    public void play(List<Zone> zones) {
        List<Zombie> zombies = getZombies(zones);

        Map<Zone, List<Zombie>> attackingZombiesPerZone = zombies.stream()
                .filter(Zombie::canFight)
                .collect(toMap(
                        Actor::getZone,
                        Collections::singletonList,
                        (l, l2) -> Stream.concat(l.stream(), l2.stream())
                                .toList()));

        attackingZombiesPerZone.forEach((zone, zombiesInZone) -> {
            List<Survivor> zoneSurvivors = zone.getSurvivors();
            handleZombieAttacks(zombies, zoneSurvivors);
        });

        // TODO: move zombies per zone
        zombies.stream()
                .filter(zombie -> !zombie.canFight())
                .forEach(Zombie::moves);
    }

    private List<Zombie> getZombies(List<Zone> zones) {
        return zones.stream()
                .map(Zone::getZombies)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void handleZombieAttacks(List<Zombie> attackingZombies, List<Survivor> survivors) {
        int remainingWounds = attackingZombies.stream()
                .mapToInt(zombie -> zombie.getType().getDamage())
                .sum();

        output.display(attackingZombies.size() + " zombies attack");

        if (survivors.size() == 1) {
            survivors.get(0).suffersInjury(remainingWounds);
            return;
        }

        Map<Survivor, Integer> woundsPerSurvivors = new HashMap<>();
        do {
            output.display("Remaining wounds to allocate: " + remainingWounds);
            Survivor survivor = actorSelection.chooseActor(survivors);

            output.display("Allocate wounds for " + survivor + " (already allocated: " + woundsPerSurvivors.getOrDefault(survivor, 0) + "):");
            int choice = choiceMaker.getChoice(0, remainingWounds);

            if (choice > 0) {
                remainingWounds -= choice;
                woundsPerSurvivors.merge(survivor, choice, Integer::sum);
            }
        } while (remainingWounds > 0);

        woundsPerSurvivors.forEach(Survivor::suffersInjury);
    }
}

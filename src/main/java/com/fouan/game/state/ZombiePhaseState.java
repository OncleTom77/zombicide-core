package com.fouan.game.state;

import com.fouan.actor.Zombie;
import com.fouan.actor.ZombiePhase;
import com.fouan.board.Zone;
import com.fouan.command.Command;
import com.fouan.game.GameResult;
import com.fouan.io.Output;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Named("zombiePhaseState")
public class ZombiePhaseState extends AbstractComputeGameResultState {

    private final ZombiePhase zombiePhase;
    private final Output output;
    private final State endGameState;
    private final State spawnZombieState;

    public ZombiePhaseState(ZombiePhase zombiePhase, Output output, @Named("endGameState") State endGameState, @Named("spawnZombieState") State spawnZombieState) {
        this.zombiePhase = zombiePhase;
        this.output = output;
        this.endGameState = endGameState;
        this.spawnZombieState = spawnZombieState;
    }

    @Override
    public State run(StateContext context) {
        List<Zone> zones = context.getZones().getZones();
        List<Command> commands = new ArrayList<>();

        List<Zombie> activatedZombies = zones.stream()
                .map(zone -> {
                    List<Zombie> attackingZombies = zone.getZombies()
                            .stream()
                            .filter(Zombie::canFight)
                            .toList();

                    if (!attackingZombies.isEmpty()) {
                        commands.addAll(zombiePhase.handleZombieAttacks(attackingZombies, zone.getSurvivors()));
                    }

                    return attackingZombies;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));

        zones.stream()
                .map(zone -> zombiePhase.handleZombieMove(zone, activatedZombies))
                .forEach(commands::addAll);

        commands.forEach(command -> {
            command.execute();
            command.executeVisual(output);
        });

        if (computeGameResult(context) != GameResult.UNDEFINED) {
            return endGameState;
        }

        return spawnZombieState;
    }
}

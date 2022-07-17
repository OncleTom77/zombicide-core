package com.fouan.game.state;

import com.fouan.actor.Survivor;
import com.fouan.board.BoardMarker;
import com.fouan.board.DangerLevel;
import com.fouan.board.ZombieSpawner;
import com.fouan.command.Command;
import com.fouan.io.Output;

import javax.inject.Named;
import java.util.List;
import java.util.Optional;

@Named("spawnZombieState")
public class SpawnZombieState implements State {

    private final ZombieSpawner zombieSpawner;
    private final Output output;
    private final State endPhaseState;

    public SpawnZombieState(ZombieSpawner zombieSpawner, Output output, @Named("endPhaseState") State endPhaseState) {
        this.zombieSpawner = zombieSpawner;
        this.output = output;
        this.endPhaseState = endPhaseState;
    }

    @Override
    public State run(StateContext context) {
        DangerLevel dangerLevel = context.getLivingSurvivors()
                .stream()
                .map(Survivor::getDangerLevel)
                .max(DangerLevel::compareTo)
                .orElseThrow();

        List<Command> commands = context.getZones()
                .findZones(BoardMarker.ZOMBIE_SPAWN)
                .stream()
                .map(zone -> zombieSpawner.spawnZombies(dangerLevel, zone))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        commands.forEach(command -> {
            command.execute();
            command.executeVisual(output);
        });

        return endPhaseState;
    }
}

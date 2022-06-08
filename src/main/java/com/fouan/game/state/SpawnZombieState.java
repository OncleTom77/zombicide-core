package com.fouan.game.state;

import com.fouan.actor.Survivor;
import com.fouan.board.*;

import javax.inject.Named;

@Named("spawnZombieState")
public class SpawnZombieState implements State {

    private final ZombieSpawner zombieSpawner;
    private final State endPhaseState;

    public SpawnZombieState(ZombieSpawner zombieSpawner, @Named("endPhaseState") State endPhaseState) {
        this.zombieSpawner = zombieSpawner;
        this.endPhaseState = endPhaseState;
    }

    @Override
    public State run(StateContext context) {
        DangerLevel dangerLevel = context.getLivingSurvivors()
                .stream()
                .map(Survivor::getDangerLevel)
                .max(DangerLevel::compareTo)
                .orElseThrow();

        context.getZones()
                .findZones(BoardMarker.ZOMBIE_SPAWN)
                .forEach(zone -> zombieSpawner.spawnZombies(dangerLevel, zone));

        return endPhaseState;
    }
}

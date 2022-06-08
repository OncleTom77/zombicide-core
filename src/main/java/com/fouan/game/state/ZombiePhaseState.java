package com.fouan.game.state;

import com.fouan.actor.ZombiePhase;
import com.fouan.game.GameResult;

import javax.inject.Named;

@Named("zombiePhaseState")
public class ZombiePhaseState extends AbstractComputeGameResultState {

    private final ZombiePhase zombiePhase;
    private final State endGameState;
    private final State spawnZombieState;

    public ZombiePhaseState(ZombiePhase zombiePhase, @Named("endGameState") State endGameState, @Named("spawnZombieState") State spawnZombieState) {
        this.zombiePhase = zombiePhase;
        this.endGameState = endGameState;
        this.spawnZombieState = spawnZombieState;
    }

    @Override
    public State run(StateContext context) {
        zombiePhase.play(context.getZones().getZones());

        if (computeGameResult(context) != GameResult.UNDEFINED) {
            return endGameState;
        }

        return spawnZombieState;
    }
}

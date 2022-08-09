package com.fouan.old.game.state.survivor.action;

import com.fouan.old.game.state.State;
import com.fouan.old.game.state.StateContext;

public abstract class SurvivorActionState implements State {

    private final State endSurvivorActionState;

    protected SurvivorActionState(State endSurvivorActionState) {
        this.endSurvivorActionState = endSurvivorActionState;
    }

    @Override
    public final State getNextState(StateContext context) {
        return endSurvivorActionState;
    }

    public abstract boolean isPossible(StateContext context);
}

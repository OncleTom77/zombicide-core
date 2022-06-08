package com.fouan.game.state.survivor.action;

import com.fouan.game.state.State;
import com.fouan.game.state.StateContext;

import javax.inject.Named;

@Named
public class SurvivorDoNothingState implements SurvivorActionState {

    private final State endSurvivorActionState;

    protected SurvivorDoNothingState(@Named("endSurvivorActionState") State endSurvivorActionState) {
        this.endSurvivorActionState = endSurvivorActionState;
    }

    @Override
    public State run(StateContext context) {
        context.setActionCounter(context.getPlayingSurvivor().getActionsPerTurn() - 1);
        return endSurvivorActionState;
    }

    @Override
    public boolean isPossible(StateContext context) {
        return true;
    }

    @Override
    public String toString() {
        return "Do nothing";
    }
}

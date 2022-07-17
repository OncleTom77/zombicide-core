package com.fouan.game.state.survivor.action;

import com.fouan.command.Command;
import com.fouan.command.SpendAllRemainingActionsCommand;
import com.fouan.game.state.State;
import com.fouan.game.state.StateContext;
import com.fouan.io.Output;

import javax.inject.Named;

@Named
public class SurvivorDoNothingState implements SurvivorActionState {

    private final State endSurvivorActionState;
    private final Output output;

    protected SurvivorDoNothingState(@Named("endSurvivorActionState") State endSurvivorActionState, Output output) {
        this.endSurvivorActionState = endSurvivorActionState;
        this.output = output;
    }

    @Override
    public State run(StateContext context) {
        Command command = new SpendAllRemainingActionsCommand(context);
        command.execute();
        command.executeVisual(output);
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

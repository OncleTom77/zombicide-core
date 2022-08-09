package com.fouan.old.game.state.survivor.action;

import com.fouan.old.command.Command;
import com.fouan.old.command.SpendAllRemainingActionsCommand;
import com.fouan.old.game.state.State;
import com.fouan.old.game.state.StateContext;

import javax.inject.Named;
import java.util.List;

import static java.util.Collections.singletonList;

@Named
public class SurvivorDoNothingState extends SurvivorActionState {

    protected SurvivorDoNothingState(@Named("endSurvivorActionState") State endSurvivorActionState) {
        super(endSurvivorActionState);
    }

    @Override
    public List<Command> run(StateContext context) {
        return singletonList(new SpendAllRemainingActionsCommand(context));
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
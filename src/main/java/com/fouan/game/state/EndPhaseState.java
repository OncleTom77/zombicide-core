package com.fouan.game.state;

import com.fouan.command.Command;
import com.fouan.command.EndPhaseCommand;

import javax.inject.Named;
import java.util.List;

import static java.util.Collections.singletonList;

@Named("endPhaseState")
public class EndPhaseState implements State {
    private final State playerActionDecisionState;

    public EndPhaseState(@Named("playerActionDecisionState") State playerActionDecisionState) {
        this.playerActionDecisionState = playerActionDecisionState;
    }

    @Override
    public List<Command> run(StateContext context) {
        return singletonList(new EndPhaseCommand(context));
    }

    @Override
    public State getNextState(StateContext context) {
        return playerActionDecisionState;
    }
}

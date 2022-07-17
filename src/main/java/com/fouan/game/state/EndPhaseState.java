package com.fouan.game.state;

import com.fouan.command.Command;
import com.fouan.command.EndPhaseCommand;
import com.fouan.io.Output;

import javax.inject.Named;

@Named("endPhaseState")
public class EndPhaseState implements State {
    private final State playerActionDecisionState;
    private final Output output;

    public EndPhaseState(@Named("playerActionDecisionState") State playerActionDecisionState, Output output) {
        this.playerActionDecisionState = playerActionDecisionState;
        this.output = output;
    }

    @Override
    public State run(StateContext context) {
        Command command = new EndPhaseCommand(context);
        command.execute();
        command.executeVisual(output);

        return playerActionDecisionState;
    }
}

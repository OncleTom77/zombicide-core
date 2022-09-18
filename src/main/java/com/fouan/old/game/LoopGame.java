package com.fouan.old.game;

import com.fouan.old.command.Command;
import com.fouan.old.command.CommandExecutor;
import com.fouan.old.game.state.State;
import com.fouan.old.game.state.StateContext;
import com.fouan.display.Output;

import javax.inject.Named;
import java.util.List;

@Named
public class LoopGame {
    private final State initialState;
    private final Output output;

    public LoopGame(@Named("initGameState") State initialState, Output output) {
        this.initialState = initialState;
        this.output = output;
    }

    public void run() {
        State state = initialState;
        StateContext context = new StateContext();
        CommandExecutor commandExecutor = new CommandExecutor(output);

        while (state != null) {
            List<Command> commands = state.run(context);
            commandExecutor.addAll(commands);
            commandExecutor.execute();
            commandExecutor.executeVisual();
            commandExecutor.commit();
            state = state.getNextState(context);
        }
    }
}

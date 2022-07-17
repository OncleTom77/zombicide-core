package com.fouan.command;

import com.fouan.actor.Survivor;
import com.fouan.game.state.StateContext;
import com.fouan.io.Output;

public class SpendAllRemainingActionsCommand implements Command {

    private final StateContext context;
    private final int actualActionCounter;

    public SpendAllRemainingActionsCommand(StateContext context) {
        this.context = context;
        this.actualActionCounter = context.getActionCounter();
    }

    @Override
    public void execute() {
        context.setActionCounter(context.getPlayingSurvivor().getActionsPerTurn());
    }

    @Override
    public void executeVisual(Output output) {
        Survivor playingSurvivor = context.getPlayingSurvivor();
        output.display(playingSurvivor + " does nothing. " + (playingSurvivor.getActionsPerTurn() - actualActionCounter) + " actions are lost.");
    }

    @Override
    public void undo() {
        context.setActionCounter(actualActionCounter);
    }
}

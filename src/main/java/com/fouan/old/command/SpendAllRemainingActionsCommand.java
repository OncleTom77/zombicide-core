package com.fouan.old.command;

import com.fouan.old.actor.Survivor;
import com.fouan.old.game.state.StateContext;
import com.fouan.display.Output;

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

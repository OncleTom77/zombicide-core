package com.fouan.command;

import com.fouan.actor.Survivor;
import com.fouan.game.state.StateContext;
import com.fouan.io.Output;

public class EndPlayerTurnCommand implements Command {
    private final StateContext context;
    private final Survivor playingSurvivor;

    public EndPlayerTurnCommand(StateContext context) {
        this.context = context;
        playingSurvivor = context.getPlayingSurvivor();
    }

    @Override
    public void execute() {
        context.getUnactivatedSurvivors().remove(playingSurvivor);
        context.getActivatedSurvivors().addLast(playingSurvivor);
        context.setActionCounter(0);
    }

    @Override
    public void executeVisual(Output output) {
    }

    @Override
    public void undo() {
        context.getUnactivatedSurvivors().addFirst(playingSurvivor);
        context.getActivatedSurvivors().remove(playingSurvivor);
        context.setActionCounter(playingSurvivor.getActionsPerTurn());
    }
}

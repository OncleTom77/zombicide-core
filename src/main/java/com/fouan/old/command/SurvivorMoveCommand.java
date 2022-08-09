package com.fouan.old.command;

import com.fouan.old.board.Zone;
import com.fouan.old.game.state.StateContext;
import com.fouan.display.Output;

public class SurvivorMoveCommand extends MoveCommand {

    private final StateContext context;

    public SurvivorMoveCommand(StateContext context, Zone destination) {
        super(context.getPlayingSurvivor(), destination);
        this.context = context;
    }

    @Override
    public void execute() {
        super.execute();
        context.setActionCounter(context.getActionCounter() + 1);
    }

    @Override
    public void executeVisual(Output output) {
        super.executeVisual(output);
    }

    @Override
    public void undo() {
        super.execute();
        context.setActionCounter(context.getActionCounter() - 1);
    }
}

package com.fouan.command;

import com.fouan.board.Zone;
import com.fouan.game.state.StateContext;
import com.fouan.io.Output;

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

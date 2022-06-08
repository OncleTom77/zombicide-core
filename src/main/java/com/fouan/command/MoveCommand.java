package com.fouan.command;

import com.fouan.actor.Actor;
import com.fouan.board.Zone;
import com.fouan.io.Output;

public class MoveCommand implements Command {

    private final Actor actor;
    private final Zone zone;
    private final Zone initialZone;

    public MoveCommand(Actor actor, Zone zone) {
        this.actor = actor;
        this.zone = zone;
        initialZone = actor.getZone();
    }

    @Override
    public void execute() {
        actor.changesZone(zone);
    }

    @Override
    public void executeVisual(Output output) {
        output.display(actor + " moves from " + initialZone + " to " + zone);
    }

    @Override
    public void undo() {
        actor.changesZone(initialZone);
    }
}

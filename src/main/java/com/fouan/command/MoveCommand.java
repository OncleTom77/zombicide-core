package com.fouan.command;

import com.fouan.actor.Actor;
import com.fouan.board.Zone;
import com.fouan.io.Output;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class MoveCommand implements Command {

    private final Actor actor;
    private final Zone destination;
    private final Zone initialZone;

    public MoveCommand(Actor actor, Zone destination) {
        this.actor = actor;
        this.destination = destination;
        initialZone = actor.getZone();
    }

    @Override
    public void execute() {
        initialZone.removeActor(actor);
        destination.addActor(actor);
        actor.setZone(destination);
    }

    @Override
    public void executeVisual(Output output) {
        output.display(actor + " moves from " + initialZone + " to " + destination);
    }

    @Override
    public void undo() {
        destination.removeActor(actor);
        initialZone.addActor(actor);
        actor.setZone(initialZone);
    }
}

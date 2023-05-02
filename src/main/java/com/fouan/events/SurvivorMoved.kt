package com.fouan.events;

import com.fouan.actors.ActorId;
import com.fouan.zones.Position;
import com.fouan.zones.Zone;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class SurvivorMoved
        extends Event<SurvivorMoved>
        implements ZoneEvent, ActorEvent, ActionEvent {

    private final ActorId survivorId;
    private final Position position;

    public SurvivorMoved(int turn, ActorId survivorId, Position position) {
        super(turn);
        this.survivorId = survivorId;
        this.position = position;
    }

    @NotNull
    @Override
    public ActorId getActorId() {
        return survivorId;
    }

    public Position getPosition() {
        return position;
    }
}

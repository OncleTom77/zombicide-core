package com.fouan.events;

import com.fouan.actors.ActorId;
import com.fouan.zones.Zone;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class SurvivorMoved
        extends Event<SurvivorMoved>
        implements ZoneEvent, ActorEvent, ActionEvent {

    private final ActorId survivorId;
    private final Zone zone;

    public SurvivorMoved(int turn, ActorId survivorId, Zone zone) {
        super(turn);
        this.survivorId = survivorId;
        this.zone = zone;
    }

    @NotNull
    @Override
    public ActorId getActorId() {
        return survivorId;
    }
}

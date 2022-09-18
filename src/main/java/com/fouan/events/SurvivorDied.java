package com.fouan.events;

import com.fouan.actors.ActorId;
import lombok.Getter;

@Getter
public final class SurvivorDied
        extends Event<SurvivorDied>
        implements ActorEvent {

    private final ActorId survivorId;

    public SurvivorDied(int turn, ActorId survivorId) {
        super(turn);
        this.survivorId = survivorId;
    }
}

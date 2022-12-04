package com.fouan.events;

import com.fouan.actors.ActorId;

public final class SurvivorsTurnEnded
        extends Event<SurvivorsTurnEnded>
        implements GameEvent {

    private final ActorId survivorId;

    public SurvivorsTurnEnded(int turn, ActorId survivorId) {
        super(turn);
        this.survivorId = survivorId;
    }

    public ActorId getActorId() {
        return survivorId;
    }
}

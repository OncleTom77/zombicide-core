package com.fouan.events;

import com.fouan.actors.ActorId;
import com.fouan.actors.Survivor;
import lombok.Getter;

@Getter
public final class SurvivorsTurnStarted
        extends Event<SurvivorsTurnStarted>
        implements ActorEvent, GameEvent {

    private final ActorId survivorId;

    public SurvivorsTurnStarted(int turn, ActorId survivorId) {
        super(turn);
        this.survivorId = survivorId;
    }
}

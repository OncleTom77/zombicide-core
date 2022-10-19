package com.fouan.events;

import com.fouan.actors.Survivor;
import lombok.Getter;

@Getter
public final class SurvivorsTurnStarted
        extends Event<SurvivorsTurnStarted>
        implements ActorEvent, GameEvent {

    private final Survivor survivor;

    public SurvivorsTurnStarted(int turn, Survivor survivor) {
        super(turn);
        this.survivor = survivor;
    }
}

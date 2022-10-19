package com.fouan.events;

import com.fouan.actors.Survivor;

public final class SurvivorsTurnEnded
        extends Event<SurvivorsTurnEnded>
        implements GameEvent {

    private final Survivor survivor;

    public SurvivorsTurnEnded(int turn, Survivor survivor) {
        super(turn);
        this.survivor = survivor;
    }
}

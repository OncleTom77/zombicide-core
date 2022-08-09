package com.fouan.events;

public final class SurvivorsTurnStarted
        extends Event<SurvivorsTurnStarted>
        implements GameEvent {

    public SurvivorsTurnStarted(int turn) {
        super(turn);
    }
}

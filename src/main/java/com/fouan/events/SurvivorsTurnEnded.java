package com.fouan.events;

public final class SurvivorsTurnEnded
        extends Event<SurvivorsTurnEnded>
        implements GameEvent {

    public SurvivorsTurnEnded(int turn) {
        super(turn);
    }
}

package com.fouan.events;

public final class ZombiesTurnStarted
        extends Event<ZombiesTurnStarted>
        implements GameEvent {

    public ZombiesTurnStarted(int turn) {
        super(turn);
    }
}

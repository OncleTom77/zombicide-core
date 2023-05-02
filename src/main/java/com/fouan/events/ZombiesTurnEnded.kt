package com.fouan.events;

public final class ZombiesTurnEnded
        extends Event<ZombiesTurnEnded>
        implements GameEvent {

    public ZombiesTurnEnded(int turn) {
        super(turn);
    }
}

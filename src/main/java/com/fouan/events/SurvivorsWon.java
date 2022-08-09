package com.fouan.events;

import lombok.Getter;

@Getter
public final class SurvivorsWon
        extends Event<SurvivorsWon>
        implements EndGameEvent {

    private final int timestamp;

    public SurvivorsWon(int turn, int timestamp) {
        super(turn);
        this.timestamp = timestamp;
    }
}

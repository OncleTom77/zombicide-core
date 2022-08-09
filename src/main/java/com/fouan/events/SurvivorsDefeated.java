package com.fouan.events;

import lombok.Getter;

@Getter
public final class SurvivorsDefeated
        extends Event<SurvivorsDefeated>
        implements EndGameEvent {

    private final int timestamp;

    public SurvivorsDefeated(int turn, int timestamp) {
        super(turn);
        this.timestamp = timestamp;
    }
}

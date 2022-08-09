package com.fouan.events;

import lombok.Getter;

@Getter
public final class GameStarted extends Event<GameStarted> {

    private final int timestamp;

    public GameStarted(int turn, int timestamp) {
        super(turn);
        this.timestamp = timestamp;
    }
}

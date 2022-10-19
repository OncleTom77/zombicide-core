package com.fouan.events;

public abstract class Event<T extends Event> {

    private final int turn;

    public Event(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return this.turn;
    }
}

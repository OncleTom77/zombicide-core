package com.fouan.events;

import com.fouan.actions.Actions;

public final class ActionChosen extends Event<ActionChosen> implements GameEvent{

    private final Actions action;

    public ActionChosen(int turn, Actions action) {
        super(turn);
        this.action = action;
    }

    public Actions getAction() {
        return this.action;
    }
}

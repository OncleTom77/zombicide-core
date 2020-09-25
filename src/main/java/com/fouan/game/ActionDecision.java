package com.fouan.game;

import com.fouan.action.Action;

import javax.inject.Named;

@Named
public class ActionDecision {

    private final Action action;

    public ActionDecision(Action action) {
        this.action = action;
    }

    public Action next() {
        return action;
    }
}

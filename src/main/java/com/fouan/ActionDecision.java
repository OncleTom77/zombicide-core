package com.fouan;

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

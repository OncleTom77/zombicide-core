package com.fouan;

import javax.inject.Named;

@Named
public class CommandInput {

    private final Action action;

    public CommandInput(Action action) {
        this.action = action;
    }

    public Action next() {
        return action;
    }
}

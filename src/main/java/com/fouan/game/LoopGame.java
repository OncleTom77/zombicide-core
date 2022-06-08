package com.fouan.game;

import com.fouan.game.state.State;
import com.fouan.game.state.StateContext;

import javax.inject.Named;

@Named
public class LoopGame {
    private final State initialState;

    public LoopGame(@Named("initGameState") State initialState) {
        this.initialState = initialState;
    }

    public void run() {
        State state = initialState;
        StateContext stateContext = new StateContext();

        while (state != null) {
            state = state.run(stateContext);
        }
    }
}

package com.fouan.game.state;

public interface State {

    State run(StateContext context);
}

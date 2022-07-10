package com.fouan.game.state;

import com.fouan.command.Command;

import java.util.List;

public interface State {

    List<Command> run(StateContext context);

    State getNextState(StateContext context);
}

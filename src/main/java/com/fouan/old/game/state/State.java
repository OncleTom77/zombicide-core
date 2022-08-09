package com.fouan.old.game.state;

import com.fouan.old.command.Command;

import java.util.List;

public interface State {

    List<Command> run(StateContext context);

    State getNextState(StateContext context);
}

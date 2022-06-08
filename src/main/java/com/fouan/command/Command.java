package com.fouan.command;

import com.fouan.io.Output;

public interface Command {

    void execute();

    //TODO: handle this in LoopGame
    void executeVisual(Output output);

    void undo();
}

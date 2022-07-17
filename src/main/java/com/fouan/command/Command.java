package com.fouan.command;

import com.fouan.io.Output;

public interface Command {

    void execute();

    void executeVisual(Output output);

    void undo();
}

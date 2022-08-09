package com.fouan.old.command;

import com.fouan.display.Output;

public interface Command {

    void execute();

    void executeVisual(Output output);

    void undo();
}

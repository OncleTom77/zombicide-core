package com.fouan.old.command;

import com.fouan.display.Output;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Queue;

public class CommandExecutor {

    private final Output output;
    private final Deque<Command> commandHistory;
    private final Queue<Command> newCommands;

    public CommandExecutor(Output output) {
        this.output = output;
        commandHistory = new ArrayDeque<>();
        newCommands = new ArrayDeque<>();
    }

    public void add(Command command) {
        newCommands.add(command);
    }

    public void addAll(Collection<Command> commands) {
        newCommands.addAll(commands);
    }

    public void execute() {
        newCommands.forEach(Command::execute);
    }

    public void executeVisual() {
        newCommands.forEach(command -> command.executeVisual(output));
    }

    public void commit() {
        while (!newCommands.isEmpty()) {
            commandHistory.push(newCommands.poll());
        }
    }

    public void rollback() {
        if (!commandHistory.isEmpty()) {
            commandHistory.pop().undo();
        }
    }
}

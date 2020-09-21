package com.fouan;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class LoopGame {

    private final CommandInput commandInput;
    private final Output output;

    @Inject
    public LoopGame(CommandInput commandInput, Output output) {
        this.commandInput = commandInput;
        this.output = output;
    }

    public void run(Board board) {
        do {
            board.displayBoard();
            output.println("new turn");

            // survivors' phase
            commandInput.next().execute(board);
            // check potential survivors victory

            // zombies' phase
            // check potential survivors defeat

            // zombies invasion
            // check potential survivors defeat
        } while (!board.isObjectiveComplete());
        output.println("You won. End of the game!");
    }
}

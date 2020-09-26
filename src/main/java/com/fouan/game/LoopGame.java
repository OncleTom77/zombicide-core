package com.fouan.game;

import com.fouan.board.Board;
import com.fouan.io.Output;

import javax.inject.Named;

@Named
public class LoopGame {

    private final ActionDecision actionDecision;
    private final Output output;

    public LoopGame(ActionDecision actionDecision, Output output) {
        this.actionDecision = actionDecision;
        this.output = output;
    }

    public void run(Board board) {
        do {
            board.displayBoard();
            output.display("New turn");

            // survivors' phase
            actionDecision.next(board.getSurvivor()).execute(board.getSurvivor(), board);

            // check potential survivors victory
            if (board.isObjectiveComplete()) {
                break;
            }

            // zombies' phase
            board.playZombiePhase();
            // check potential survivors defeat

            // zombies invasion
            // check potential survivors defeat
        } while (true);
        output.display("You won. End of the game!");
    }
}

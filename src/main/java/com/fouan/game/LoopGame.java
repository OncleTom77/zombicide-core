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
            board.getSurvivor()
                    .displayWounds();

            // survivors' phase
            actionDecision.next(board.getSurvivor())
                    .execute(board.getSurvivor(), board);

            // check potential survivors victory
            if (board.isObjectiveComplete()) {
                output.display("You won!");
                break;
            }

            // zombies' phase
            board.playZombiePhase();
            // check potential survivors defeat
            if (!board.hasSurvivorAlive()) {
                output.display("You lose!");
                break;
            }

            // zombies invasion
            // check potential survivors defeat
        } while (true);

        output.display("End of the game.");
    }
}

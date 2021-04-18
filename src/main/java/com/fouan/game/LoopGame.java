package com.fouan.game;

import com.fouan.board.Board;
import com.fouan.character.Survivor;
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
        GameResult gameResult = GameResult.UNDEFINED;
        do {
            output.display("New turn");

            // survivors' phase
            Survivor survivor = board.getSurvivor();
            for (int i = 0; i < survivor.getActionsPerTurn() && gameResult == GameResult.UNDEFINED; i++) {
                board.displayBoard();
                output.display("Action N°" + (i + 1));
                actionDecision.next(survivor)
                        .execute(survivor, board);

                // check potential survivors victory
                gameResult = board.computeGameResult();
            }

            if (gameResult != GameResult.UNDEFINED) {
                break;
            }

            // zombies' phase
            board.playZombiePhase();
            // check potential survivors defeat
            gameResult = board.computeGameResult();

            // zombies invasion
            // check potential survivors defeat
        } while (gameResult == GameResult.UNDEFINED);

        if (gameResult == GameResult.SURVIVORS_VICTORY) {
            output.display("You won!");
        } else if (gameResult == GameResult.SURVIVORS_DEFEAT) {
            output.display("You lose!");
        }
        output.display("End of the game.");
    }
}

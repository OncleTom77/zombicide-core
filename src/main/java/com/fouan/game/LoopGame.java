package com.fouan.game;

import com.fouan.board.Board;
import com.fouan.actor.Survivor;
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
            gameResult = survivorsPhase(board, gameResult);
            if (gameResult != GameResult.UNDEFINED) {
                break;
            }

            // zombies' phase
            board.playZombiesPhase();
            // check potential survivors defeat
            gameResult = board.computeGameResult();

            // zombies invasion
            board.spawnZombies();
            // check potential survivors defeat
        } while (gameResult == GameResult.UNDEFINED);

        if (gameResult == GameResult.SURVIVORS_VICTORY) {
            output.display("You won!");
        } else if (gameResult == GameResult.SURVIVORS_DEFEAT) {
            output.display("You lose!");
        }
        output.display("End of the game.");
    }

    private GameResult survivorsPhase(Board board, GameResult gameResult) {
        Survivor survivor = board.getSurvivor();
        for (int i = 0; i < survivor.getActionsPerTurn() && gameResult == GameResult.UNDEFINED; i++) {
            board.displayBoard();
            output.display("Action N°" + (i + 1));
            actionDecision.next(survivor)
                    .execute(survivor, board);

            // check potential survivors victory
            gameResult = board.computeGameResult();
        }
        return gameResult;
    }
}

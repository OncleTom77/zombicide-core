package com.fouan.game;

import com.fouan.board.Board;
import com.fouan.actor.Survivor;
import com.fouan.io.Output;

import javax.inject.Named;
import java.util.List;

@Named
public class LoopGame {

    private final ActionDecision actionDecision;
    private final Output output;

    public LoopGame(ActionDecision actionDecision, Output output) {
        this.actionDecision = actionDecision;
        this.output = output;
    }

    public void run(Board board) {
        GameResult gameResult;
        do {
            output.display("New turn");

            // survivors' phase
            gameResult = survivorsPhase(board);
            if (gameResult != GameResult.UNDEFINED) {
                break;
            }

            // zombies' phase
            board.playZombiesPhase();
            // check potential survivors defeat
            gameResult = board.computeGameResult();
            if (gameResult != GameResult.UNDEFINED) {
                break;
            }

            // zombies invasion
            board.spawnZombies();
            // check potential survivors defeat
        } while (gameResult == GameResult.UNDEFINED);

        switch (gameResult) {
            case SURVIVORS_VICTORY -> output.display("You won!");
            case SURVIVORS_DEFEAT -> output.display("You lose!");
        }
        output.display("End of the game.");
    }

    private GameResult survivorsPhase(Board board) {
        List<Survivor> survivors = board.getLivingSurvivors();
        for (Survivor survivor : survivors) {
            output.display(survivor.toString());
            for (int i = 0; i < survivor.getActionsPerTurn(); i++) {
                board.displayBoard();
                output.display("Action N°" + (i + 1));
                actionDecision.next(survivor)
                        .execute(survivor, board);

                // check potential survivors victory
                GameResult gameResult = board.computeGameResult();
                if (gameResult != GameResult.UNDEFINED) {
                    return gameResult;
                }
            }
        }
        return GameResult.UNDEFINED;
    }
}

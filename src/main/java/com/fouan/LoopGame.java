package com.fouan;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class LoopGame {

    private final ActionDecision actionDecision;
    private final Output output;

    @Inject
    public LoopGame(ActionDecision actionDecision, Output output) {
        this.actionDecision = actionDecision;
        this.output = output;
    }

    public void run(Board board) {
        do {
            board.displayBoard();
            output.println("new turn");

            // survivors' phase
            actionDecision.next().execute(board.getSurvivor());

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
        output.println("You won. End of the game!");
    }
}

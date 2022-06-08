package com.fouan.game.state;

import com.fouan.game.GameResult;
import com.fouan.io.Output;

import javax.inject.Named;

@Named("endGameState")
public class EndGameState extends AbstractComputeGameResultState {

    private final Output output;

    public EndGameState(Output output) {
        this.output = output;
    }

    @Override
    public State run(StateContext context) {
        GameResult gameResult = computeGameResult(context);
        switch (gameResult) {
            case SURVIVORS_VICTORY -> output.display("You won!");
            case SURVIVORS_DEFEAT -> output.display("You lose!");
            default -> throw new IllegalStateException();
        }
        output.display("End of the game.");
        return null;
    }
}

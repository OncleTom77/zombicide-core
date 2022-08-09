package com.fouan.old.game.state;

import com.fouan.old.command.Command;
import com.fouan.old.game.GameResult;
import com.fouan.display.Output;

import javax.inject.Named;
import java.util.Collections;
import java.util.List;

@Named("endGameState")
public class EndGameState extends AbstractComputeGameResultState {

    private final Output output;

    public EndGameState(Output output) {
        this.output = output;
    }

    @Override
    public List<Command> run(StateContext context) {
        GameResult gameResult = computeGameResult(context);
        switch (gameResult) {
            case SURVIVORS_VICTORY -> output.display("You won!");
            case SURVIVORS_DEFEAT -> output.display("You lose!");
            default -> throw new IllegalStateException();
        }
        output.display("End of the game.");
        return Collections.emptyList();
    }

    @Override
    public State getNextState(StateContext context) {
        return null;
    }
}

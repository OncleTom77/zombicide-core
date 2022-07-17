package com.fouan.game.state.survivor.action;

import com.fouan.command.Command;
import com.fouan.command.EndPlayerTurnCommand;
import com.fouan.game.GameResult;
import com.fouan.game.state.AbstractComputeGameResultState;
import com.fouan.game.state.State;
import com.fouan.game.state.StateContext;
import com.fouan.io.Output;

import javax.inject.Named;

@Named("endSurvivorActionState")
public class EndSurvivorActionState extends AbstractComputeGameResultState {

    private final State endGameState;
    private final State zombiePhaseState;
    private final State playerActionDecisionState;
    private final Output output;

    public EndSurvivorActionState(@Named("endGameState") State endGameState,
                                  @Named("zombiePhaseState") State zombiePhaseState,
                                  @Named("playerActionDecisionState") State playerActionDecisionState,
                                  Output output) {
        this.endGameState = endGameState;
        this.zombiePhaseState = zombiePhaseState;
        this.playerActionDecisionState = playerActionDecisionState;
        this.output = output;
    }

    @Override
    public State run(StateContext context) {
        if (context.getActionCounter() >= context.getPlayingSurvivor().getActionsPerTurn()) {
            Command command = new EndPlayerTurnCommand(context);
            command.execute();
            command.executeVisual(output);
        }

        if (computeGameResult(context) != GameResult.UNDEFINED) {
            return endGameState;
        } else if (context.allSurvivorsActivated()) {
            return zombiePhaseState;
        }
        return playerActionDecisionState;
    }
}

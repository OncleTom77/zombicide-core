package com.fouan.game.state.survivor.action;

import com.fouan.actor.Survivor;
import com.fouan.game.GameResult;
import com.fouan.game.state.AbstractComputeGameResultState;
import com.fouan.game.state.State;
import com.fouan.game.state.StateContext;

import javax.inject.Named;

@Named("endSurvivorActionState")
public class EndSurvivorActionState extends AbstractComputeGameResultState {

    private final State endGameState;
    private final State zombiePhaseState;
    private final State playerActionDecisionState;

    public EndSurvivorActionState(@Named("endGameState") State endGameState,
                                  @Named("zombiePhaseState") State zombiePhaseState,
                                  @Named("playerActionDecisionState") State playerActionDecisionState) {
        this.endGameState = endGameState;
        this.zombiePhaseState = zombiePhaseState;
        this.playerActionDecisionState = playerActionDecisionState;
    }

    @Override
    public State run(StateContext context) {
        context.setActionCounter(context.getActionCounter() + 1);
        if (context.getActionCounter() >= context.getPlayingSurvivor().getActionsPerTurn()) {
            endPlayerTurn(context);
        }

        if (computeGameResult(context) != GameResult.UNDEFINED) {
            return endGameState;
        } else if (context.allSurvivorsActivated()) {
            return zombiePhaseState;
        }
        return playerActionDecisionState;
    }

    private void endPlayerTurn(StateContext context) {
        Survivor playingSurvivor = context.getPlayingSurvivor();
        context.getUnactivatedSurvivors().remove(playingSurvivor);
        context.getActivatedSurvivors().addLast(playingSurvivor);
        context.setActionCounter(0);
    }
}

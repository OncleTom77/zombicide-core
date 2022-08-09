package com.fouan.old.game.state;

import com.fouan.old.command.Command;
import com.fouan.old.command.EndPlayerTurnCommand;
import com.fouan.old.game.GameResult;

import javax.inject.Named;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

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
    public List<Command> run(StateContext context) {
        if (context.getActionCounter() >= context.getPlayingSurvivor().getActionsPerTurn()) {
            return singletonList(new EndPlayerTurnCommand(context));
        }
        return emptyList();
    }

    @Override
    public State getNextState(StateContext context) {
        if (computeGameResult(context) != GameResult.UNDEFINED) {
            return endGameState;
        } else if (context.allSurvivorsActivated()) {
            return zombiePhaseState;
        }
        return playerActionDecisionState;
    }
}

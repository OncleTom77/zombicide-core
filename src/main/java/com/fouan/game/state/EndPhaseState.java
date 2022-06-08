package com.fouan.game.state;

import com.fouan.actor.Survivor;

import javax.inject.Named;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

@Named("endPhaseState")
public class EndPhaseState implements State {
    private final State playerActionDecisionState;

    public EndPhaseState(@Named("playerActionDecisionState") State playerActionDecisionState) {
        this.playerActionDecisionState = playerActionDecisionState;
    }

    @Override
    public State run(StateContext context) {
        prepareNextTurn(context);

        return playerActionDecisionState;
    }

    private void prepareNextTurn(StateContext context) {
        Deque<Survivor> activatedSurvivors = context.getActivatedSurvivors();
        Deque<Survivor> unactivatedSurvivors = context.getUnactivatedSurvivors();

        Deque<Survivor> nextTurnSurvivors = activatedSurvivors.stream()
                .filter(survivor -> !survivor.isDead())
                .collect(Collectors.toCollection(ArrayDeque::new));
        Survivor previousFirstPlayer = nextTurnSurvivors.pollFirst();
        nextTurnSurvivors.addLast(previousFirstPlayer);

        unactivatedSurvivors.addAll(nextTurnSurvivors);
        activatedSurvivors.clear();
    }
}

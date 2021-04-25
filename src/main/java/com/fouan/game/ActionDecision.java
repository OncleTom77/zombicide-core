package com.fouan.game;

import com.fouan.action.Action;
import com.fouan.actor.Survivor;
import com.fouan.io.ChoiceMaker;
import com.fouan.io.Output;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class ActionDecision {

    private final List<Action> actions;
    private final Output output;
    private final ChoiceMaker choiceMaker;

    public ActionDecision(List<Action> actions, Output output, ChoiceMaker choiceMaker) {
        this.actions = actions;
        this.output = output;
        this.choiceMaker = choiceMaker;
    }

    public Action next(Survivor survivor) {
        List<Action> possibleActions = actions.stream()
                .filter(action -> action.isPossible(survivor))
                .collect(Collectors.toList());

        if (possibleActions.size() == 1) {
            return possibleActions.get(0);
        }

        return getChosenAction(possibleActions);
    }

    private Action getChosenAction(List<Action> possibleActions) {
        output.display("Choose your action:");
        for (int i = 0; i < possibleActions.size(); i++) {
            output.display(i + ": " + possibleActions.get(i));
        }

        int choice = choiceMaker.getChoice(0, possibleActions.size() - 1);
        return possibleActions.get(choice);
    }
}

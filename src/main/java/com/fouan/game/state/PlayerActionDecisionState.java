package com.fouan.game.state;

import com.fouan.actor.Survivor;
import com.fouan.game.state.survivor.action.SurvivorActionState;
import com.fouan.io.ChoiceMaker;
import com.fouan.io.Output;
import org.springframework.context.annotation.Lazy;

import javax.inject.Named;
import java.util.List;

@Named("playerActionDecisionState")
public class PlayerActionDecisionState extends AbstractComputeGameResultState {

    private final Output output;
    private final List<SurvivorActionState> survivorActionStates;
    private final ChoiceMaker choiceMaker;

    public PlayerActionDecisionState(Output output,
                                     ChoiceMaker choiceMaker,
                                     @Lazy List<SurvivorActionState> survivorActionStates) {
        this.output = output;
        this.survivorActionStates = survivorActionStates;
        this.choiceMaker = choiceMaker;
    }

    @Override
    public State run(StateContext context) {
        Survivor playingSurvivor = context.getPlayingSurvivor();
        int actionCounter = context.getActionCounter();

        context.getZones().displayZones(output);
        output.display(playingSurvivor.toString());
        output.display("Action N°" + (actionCounter + 1));

        List<SurvivorActionState> possibleActionStates = survivorActionStates.stream()
                .filter(survivorActionState -> survivorActionState.isPossible(context))
                .toList();
        return getChosenActionState(possibleActionStates);
    }

    private SurvivorActionState getChosenActionState(List<SurvivorActionState> survivorActionStates) {
        if (survivorActionStates.size() == 1) {
            return survivorActionStates.get(0);
        }

        output.display("Choose your action:");
        for (int i = 0; i < survivorActionStates.size(); i++) {
            output.display(i + ": " + survivorActionStates.get(i));
        }

        int choice = choiceMaker.getChoice(0, survivorActionStates.size() - 1);
        return survivorActionStates.get(choice);
    }
}

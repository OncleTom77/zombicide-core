package com.fouan.game.state.survivor.action;

import com.fouan.actor.Survivor;
import com.fouan.board.Zone;
import com.fouan.command.Command;
import com.fouan.command.SurvivorMoveCommand;
import com.fouan.game.state.State;
import com.fouan.game.state.StateContext;
import com.fouan.io.ChoiceMaker;
import com.fouan.io.Output;

import javax.inject.Named;
import java.util.List;

import static java.util.Collections.singletonList;

@Named
public class SurvivorMoveState extends SurvivorActionState {
    private final Output output;
    private final ChoiceMaker choiceMaker;

    public SurvivorMoveState(Output output, ChoiceMaker choiceMaker, @Named("endSurvivorActionState") State endSurvivorActionState) {
        super(endSurvivorActionState);
        this.output = output;
        this.choiceMaker = choiceMaker;
    }

    @Override
    public List<Command> run(StateContext context) {
        Survivor playingSurvivor = context.getPlayingSurvivor();
        List<Zone> connectedZones = playingSurvivor.getZone().getConnectedZones();

        output.display("You are here: " + playingSurvivor.getZone());
        output.display("Choose between these zones:");
        for (int i = 0; i < connectedZones.size(); i++) {
            output.display(i + ": " + connectedZones.get(i));
        }

        int choice = choiceMaker.getChoice(0, connectedZones.size() - 1);
        Zone chosenZone = connectedZones.get(choice);

        return singletonList(new SurvivorMoveCommand(context, chosenZone));
    }

    @Override
    public boolean isPossible(StateContext context) {
        return !context.getPlayingSurvivor()
                .getZone()
                .getConnectedZones()
                .isEmpty();
    }

    @Override
    public String toString() {
        return "Move";
    }
}

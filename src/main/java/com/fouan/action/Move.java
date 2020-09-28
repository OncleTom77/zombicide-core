package com.fouan.action;

import com.fouan.board.Board;
import com.fouan.io.ChoiceMaker;
import com.fouan.io.Output;
import com.fouan.board.Zone;
import com.fouan.character.Survivor;

import javax.inject.Named;
import java.util.List;

@Named
public class Move implements Action {

    private final Output output;
    private final ChoiceMaker choiceMaker;

    public Move(Output output, ChoiceMaker choiceMaker) {
        this.output = output;
        this.choiceMaker = choiceMaker;
    }

    @Override
    public boolean isPossible(Survivor survivor) {
        return !survivor.getZone().getConnectedZones().isEmpty();
    }

    @Override
    public void execute(Survivor survivor, Board board) {
        List<Zone> connectedZones = survivor.getZone().getConnectedZones();

        output.display("You are here: " + survivor.getZone());
        output.display("Choose between these zones:");
        for (int i = 0; i < connectedZones.size(); i++) {
            output.display(i + ": " + connectedZones.get(i));
        }

        int choice = choiceMaker.getChoice(0, connectedZones.size() - 1);
        survivor.changesZone(connectedZones.get(choice));
    }

    @Override
    public String toString() {
        return "Move";
    }
}

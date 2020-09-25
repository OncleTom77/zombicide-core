package com.fouan.action;

import com.fouan.io.Input;
import com.fouan.io.Output;
import com.fouan.board.Zone;
import com.fouan.character.Survivor;

import javax.inject.Named;
import java.util.List;

@Named
public class Move implements Action {

    private final Input input;
    private final Output output;

    public Move(Input input, Output output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void execute(Survivor survivor) {
        List<Zone> connectedZones = survivor.getZone().getConnectedZones();

        output.display("You are here: " + survivor.getZone());
        output.display("Choose between these zones:");
        for (int i = 0; i < connectedZones.size(); i++) {
            output.display(i + ": " + connectedZones.get(i));
        }

        int choice = getChoice(connectedZones.size() - 1);
        survivor.changeZone(connectedZones.get(choice));
    }

    private int getChoice(int max) {
        do {
            int choice = input.read();

            if (choice >= 0 && choice <= max) {
                return choice;
            }
            output.display("Try again");
        } while (true);
    }
}

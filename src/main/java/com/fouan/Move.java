package com.fouan;

import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;
import java.util.List;

@Named
public class Move implements Action {

    private final Input input;
    private final Output output;

    @Autowired
    public Move(Input input, Output output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void execute(Survivor survivor) {
        List<Zone> connectedZones = survivor.getZone().getConnectedZones();

        output.println("You are here: " + survivor.getZone());
        output.println("Choose between these zones:");
        for (int i = 0; i < connectedZones.size(); i++) {
            output.println(i + ": " + connectedZones.get(i));
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
            output.println("Try again");
        } while (true);
    }
}

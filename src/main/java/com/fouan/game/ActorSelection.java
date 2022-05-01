package com.fouan.game;

import com.fouan.actor.Actor;
import com.fouan.io.ChoiceMaker;
import com.fouan.io.Output;

import javax.inject.Named;
import java.util.List;

@Named
public class ActorSelection {

    private final Output output;
    private final ChoiceMaker choiceMaker;

    public ActorSelection(Output output, ChoiceMaker choiceMaker) {
        this.output = output;
        this.choiceMaker = choiceMaker;
    }

    public <T extends Actor> T chooseActor(List<T> actors) {
        output.display("Choose actor:");

        for (int i = 0; i < actors.size(); i++) {
            output.display(i + ": " + actors.get(i));
        }

        int choice = choiceMaker.getChoice(0, actors.size() - 1);
        return actors.get(choice);
    }
}

package com.fouan.command;

import com.fouan.actor.Survivor;
import com.fouan.game.state.StateContext;
import com.fouan.io.Output;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class EndPhaseCommand implements Command {

    private final StateContext context;
    private List<Survivor> activatedSurvivorsCopy;

    public EndPhaseCommand(StateContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        Deque<Survivor> activatedSurvivors = context.getActivatedSurvivors();
        Deque<Survivor> unactivatedSurvivors = context.getUnactivatedSurvivors();
        activatedSurvivorsCopy = activatedSurvivors.stream().toList();

        Deque<Survivor> nextTurnSurvivors = activatedSurvivors.stream()
                .filter(survivor -> !survivor.isDead())
                .collect(Collectors.toCollection(ArrayDeque::new));
        Survivor previousFirstPlayer = nextTurnSurvivors.pollFirst();
        nextTurnSurvivors.addLast(previousFirstPlayer);

        unactivatedSurvivors.addAll(nextTurnSurvivors);
        activatedSurvivors.clear();
    }

    @Override
    public void executeVisual(Output output) {
        output.display(context.getUnactivatedSurvivors().getFirst() + " receives the First Player token");
    }

    @Override
    public void undo() {
        context.getUnactivatedSurvivors().clear();
        context.getActivatedSurvivors().addAll(activatedSurvivorsCopy);
    }
}

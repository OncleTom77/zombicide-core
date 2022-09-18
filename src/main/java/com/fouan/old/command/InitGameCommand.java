package com.fouan.old.command;

import com.fouan.old.actor.Survivor;
import com.fouan.old.board.Zones;
import com.fouan.old.game.state.StateContext;
import com.fouan.display.Output;

import java.util.List;

public class InitGameCommand implements Command {

    private final StateContext context;
    private final Zones zones;
    private final List<Survivor> survivors;

    public InitGameCommand(StateContext context, Zones zones, List<Survivor> survivors) {
        this.context = context;
        this.zones = zones;
        this.survivors = survivors;
    }

    @Override
    public void execute() {
        context.setZones(zones);
        context.setSurvivors(survivors);
        context.getUnactivatedSurvivors().addAll(survivors);
    }

    @Override
    public void executeVisual(Output output) {

    }

    @Override
    public void undo() {
        context.setZones(null);
        context.setSurvivors(null);
        context.getUnactivatedSurvivors().clear();
    }
}

package com.fouan.game.state;

import com.fouan.actor.Survivor;
import com.fouan.board.Zones;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class StateContext {
    private Zones zones;
    private List<Survivor> survivors;
    // TODO: Refactor the 2 lists below in 1 list with firstPlayer index and currentPlayerIndex
    private Deque<Survivor> unactivatedSurvivors;
    private Deque<Survivor> activatedSurvivors;
    private int actionCounter;

    public StateContext() {
        unactivatedSurvivors = new ArrayDeque<>();
        activatedSurvivors = new ArrayDeque<>();
        actionCounter = 0;
    }

    public Zones getZones() {
        return zones;
    }

    public void setZones(Zones zones) {
        this.zones = zones;
    }

    public void setSurvivors(List<Survivor> survivors) {
        this.survivors = survivors;
    }

    public Deque<Survivor> getUnactivatedSurvivors() {
        return unactivatedSurvivors;
    }

    public Deque<Survivor> getActivatedSurvivors() {
        return activatedSurvivors;
    }

    public int getActionCounter() {
        return actionCounter;
    }

    public void setActionCounter(int actionCounter) {
        this.actionCounter = actionCounter;
    }

    public Survivor getPlayingSurvivor() {
        return unactivatedSurvivors.peek();
    }

    public boolean allSurvivorsActivated() {
        return unactivatedSurvivors.isEmpty();
    }

    public List<Survivor> getLivingSurvivors() {
        return survivors.stream()
                .filter(survivor -> !survivor.isDead())
                .toList();
    }
}

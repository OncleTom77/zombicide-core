package com.fouan.game.state;

import com.fouan.actor.Survivor;
import com.fouan.board.Zone;
import com.fouan.board.Zones;
import com.fouan.weapon.Weapon;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class StateContext {
    private Zones zones;
    private List<Survivor> survivors;
    private Deque<Survivor> unactivatedSurvivors;
    private Deque<Survivor> activatedSurvivors;
    private int actionCounter;
    private Weapon selectedWeapon;
    private Zone targetZone;

    public StateContext() {
    }

    public void init(Zones zones, List<Survivor> survivors) {
        this.zones = zones;
        this.survivors = survivors;
        unactivatedSurvivors = new ArrayDeque<>(survivors);
        activatedSurvivors = new ArrayDeque<>();
        actionCounter = 0;
    }

    public Zones getZones() {
        return zones;
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

    public Weapon getSelectedWeapon() {
        return selectedWeapon;
    }

    public void setSelectedWeapon(Weapon selectedWeapon) {
        this.selectedWeapon = selectedWeapon;
    }

    public Zone getTargetZone() {
        return targetZone;
    }

    public void setTargetZone(Zone targetZone) {
        this.targetZone = targetZone;
    }
}

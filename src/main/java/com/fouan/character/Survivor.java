package com.fouan.character;

import com.fouan.board.Zone;
import com.fouan.game.Direction;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

import java.util.Arrays;
import java.util.Collection;

public class Survivor {
    public static final int LIFE_POINTS = 3;
    private final Output output;
    private Zone zone;
    private Weapon weapon;
    private int wounds;
    private int actionsPerTurn;

    public Survivor(Zone initialZone, Weapon weapon, Output output) {
        this.zone = initialZone;
        this.weapon = weapon;
        this.output = output;
        wounds = 0;
        actionsPerTurn = 3;
    }

    public void changesZone(Zone zone) {
        this.zone.removeSurvivor(this);
        this.zone = zone;
        zone.addSurvivor(this);
    }

    public long attacks() {
        return weapon.use();
    }

    public Zone getZone() {
        return zone;
    }

    public int getActionsPerTurn() {
        return actionsPerTurn;
    }

    public boolean canFight() {
        return isMeleeActionPossible() || isRangedActionPossible();
    }

    private boolean isMeleeActionPossible() {
        return zone.containsZombie() && weapon.getRange() == 0;
    }

    private boolean isRangedActionPossible() {
        return Arrays.stream(Direction.values())
                .map(direction -> zone.getAllInDirection(direction))
                .flatMap(Collection::stream)
                .filter(Zone::containsZombie)
                .map(zoneInSightWithZombie -> zoneInSightWithZombie.getPosition().computeDistance(zone.getPosition()))
                .anyMatch(distance -> distance <= weapon.getRange());
    }

    public boolean isDead() {
        return wounds >= LIFE_POINTS;
    }

    public void suffersInjury(int damageInflicted) {
        wounds += damageInflicted;
    }

    public void displayWounds() {
        this.output.display("Wounds counter: " + wounds);
    }
}

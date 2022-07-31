package com.fouan.actor;

import com.fouan.board.DangerLevel;
import com.fouan.board.Zone;
import com.fouan.game.Direction;
import com.fouan.io.Output;
import com.fouan.weapon.Weapon;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class Survivor extends Actor {
    public static final int LIFE_POINTS = 3;
    private final String name;
    private Weapon weapon;
    private int wounds;
    private int experience;
    private int actionsPerTurn;

    Survivor(Output output, Zone initialZone, String name, Weapon weapon) {
        super(output, initialZone);
        this.name = name;
        this.weapon = weapon;
        wounds = 0;
        experience = 0;
        actionsPerTurn = 3;
    }

    public int getActionsPerTurn() {
        return actionsPerTurn;
    }

    public boolean canFight() {
        return isMeleeActionPossible() || isRangedActionPossible();
    }

    private boolean isMeleeActionPossible() {
        boolean killableZombie = zone.getZombies()
                .stream()
                .anyMatch(zombie -> zombie.canBeKilledByWeapon(weapon));
        return killableZombie && weapon.isMelee();
    }

    private boolean isRangedActionPossible() {
        return Arrays.stream(Direction.values())
                .map(direction -> zone.getAllInDirection(direction))
                .flatMap(Collection::stream)
                .filter(Zone::containsZombie)
                .map(zoneInSightWithZombie -> zoneInSightWithZombie.getPosition().computeDistance(zone.getPosition()))
                .anyMatch(distance -> weapon.getRange().contains(distance));
    }

    public boolean isDead() {
        return wounds >= LIFE_POINTS;
    }

    public void suffersInjury(int damageInflicted) {
        wounds += damageInflicted;
    }

    public void heals(int woundsToHeal) {
        wounds -= woundsToHeal;
    }

    public DangerLevel getDangerLevel() {
        return DangerLevel.fromExperience(experience);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survivor survivor = (Survivor) o;
        return Objects.equals(name, survivor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Survivor " + name + " (wounds: " + wounds + ")";
    }
}

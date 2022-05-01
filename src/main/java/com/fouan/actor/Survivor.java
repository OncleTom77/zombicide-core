package com.fouan.actor;

import com.fouan.board.DangerLevel;
import com.fouan.board.Zone;
import com.fouan.game.ActorSelection;
import com.fouan.game.Direction;
import com.fouan.io.Output;
import com.fouan.weapon.AttackResult;
import com.fouan.weapon.Weapon;

import java.util.Arrays;
import java.util.Collection;

import static java.util.Collections.singletonList;

public class Survivor extends Actor {
    public static final int LIFE_POINTS = 3;
    private final String name;
    private Weapon weapon;
    private int wounds;
    private int experience;
    private int actionsPerTurn;

    Survivor(Output output, ActorSelection actorSelection, Zone initialZone, String name, Weapon weapon) {
        super(output, actorSelection, initialZone);
        this.name = name;
        this.weapon = weapon;
        wounds = 0;
        experience = 0;
        actionsPerTurn = 3;
    }

    public AttackResult attacks() {
        return weapon.use();
    }

    public int getActionsPerTurn() {
        return actionsPerTurn;
    }

    public boolean canFight() {
        return isMeleeActionPossible() || isRangedActionPossible();
    }

    private boolean isMeleeActionPossible() {
        //TODO: when we will have multiple zombie types, check that survivor can kill the zombie with its equipped weapons
        boolean killableZombie = zone.getZombies()
                .stream()
                .anyMatch(zombie -> zombie.canBeKilledByWeapon(weapon));
        return killableZombie && weapon.getRange() == 0;
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
        if (isDead()) {
            zone.removeActors(singletonList(this));
        }
    }

    public DangerLevel getDangerLevel() {
        return DangerLevel.fromExperience(experience);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public String toString() {
        return "Survivor " + name + " (wounds: " + wounds + ")";
    }
}

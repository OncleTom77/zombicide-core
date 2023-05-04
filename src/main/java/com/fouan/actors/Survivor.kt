package com.fouan.actors;

import com.fouan.weapons.Weapon;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

public final class Survivor extends Actor {

    private final String name;
    private final Weapon weapon;
    private final int experience;
    private final DangerLevel dangerLevel;
    private final int lifePoints;

    public Survivor(ActorId id, int lifePoints, String name, Weapon weapon, int experience, int actionsCount) {
        super(id, actionsCount);
        this.name = name;
        this.weapon = weapon;
        this.experience = experience;
        this.dangerLevel = DangerLevel.fromExperience(experience);
        this.lifePoints = lifePoints;
    }

    public LifeStatus getLifeStatus() {
        return lifePoints > 0 ? LifeStatus.ALIVE : LifeStatus.DEAD;
    }

    public String getName() {
        return this.name;
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    public int getExperience() {
        return this.experience;
    }

    public DangerLevel getDangerLevel() {
        return this.dangerLevel;
    }

    public int getLifePoints() {
        return this.lifePoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survivor survivor = (Survivor) o;
        return experience == survivor.experience && lifePoints == survivor.lifePoints && Objects.equals(name, survivor.name) && Objects.equals(weapon, survivor.weapon) && dangerLevel == survivor.dangerLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weapon, experience, dangerLevel, lifePoints);
    }

    @Override
    public String toString() {
        return "Survivor{" +
                "name='" + name + '\'' +
                ", lifePoints=" + lifePoints +
                '}';
    }

    public enum LifeStatus {
        ALIVE, DEAD
    }
}

package com.fouan.actors;

import com.fouan.weapons.Weapon;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public final class Survivor extends Actor {

    private final String name;
    private final Weapon weapon;
    private final int experience;
    private final DangerLevel dangerLevel;
    private final int lifePoints;
    private final int actionsCount;

    public Survivor(ActorId id, int lifePoints, String name, Weapon weapon, int experience, int actionsCount) {
        super(id);
        this.name = name;
        this.weapon = weapon;
        this.experience = experience;
        this.dangerLevel = DangerLevel.fromExperience(experience);
        this.lifePoints = lifePoints;
        this.actionsCount = actionsCount;
    }

    public LifeStatus getLifeStatus() {
        return lifePoints > 0 ? LifeStatus.ALIVE : LifeStatus.DEAD;
    }

    public enum LifeStatus {
        ALIVE, DEAD
    }
}

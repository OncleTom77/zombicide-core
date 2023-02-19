package com.fouan.actors.zombies;

import com.fouan.actors.Actor;
import com.fouan.actors.ActorId;

public abstract class Zombie extends Actor {

    protected final String name;
    protected final int damage;
    protected final int minDamageToDestroy;
    protected final int experienceProvided;

    protected Zombie(ActorId id, String name, int damage, int minDamageToDestroy, int experienceProvided, int actionsCount) {
        super(id, actionsCount);
        this.name = name;
        this.damage = damage;
        this.minDamageToDestroy = minDamageToDestroy;
        this.experienceProvided = experienceProvided;
    }

    public String getName() {
        return this.name;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getMinDamageToDestroy() {
        return this.minDamageToDestroy;
    }

    public int getExperienceProvided() {
        return this.experienceProvided;
    }

    public String toString() {
        return "Zombie(name=" + this.getName() + ", damage=" + this.getDamage() + ", minDamageToDestroy=" + this.getMinDamageToDestroy() + ", experienceProvided=" + this.getExperienceProvided() + ")";
    }
}

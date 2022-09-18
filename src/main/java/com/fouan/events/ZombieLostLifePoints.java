package com.fouan.events;

import com.fouan.actors.ActorId;
import com.fouan.weapons.Weapon;
import lombok.Getter;

@Getter
public final class ZombieLostLifePoints
        extends Event<ZombieLostLifePoints>
        implements ActorEvent {

    private final ActorId zombieId;
    private final ActorId attackerId;
    private final Weapon weaponUsed;
    private final int damage;

    public ZombieLostLifePoints(int turn, ActorId zombieId, ActorId attackerId, Weapon weaponUsed, int damage) {
        super(turn);
        this.zombieId = zombieId;
        this.attackerId = attackerId;
        this.weaponUsed = weaponUsed;
        this.damage = damage;
    }
}

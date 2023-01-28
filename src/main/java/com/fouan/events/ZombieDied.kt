package com.fouan.events;

import com.fouan.actors.ActorId;
import com.fouan.weapons.Weapon;
import lombok.Getter;

@Getter
public final class ZombieDied
        extends Event<ZombieDied>
        implements ActorEvent, ZoneEvent {

    private final ActorId zombieId;
    private final ActorId attackerId;
    private final Weapon weaponUsed;

    public ZombieDied(int turn, ActorId zombieId, ActorId attackerId, Weapon weaponUsed) {
        super(turn);
        this.zombieId = zombieId;
        this.attackerId = attackerId;
        this.weaponUsed = weaponUsed;
    }
}

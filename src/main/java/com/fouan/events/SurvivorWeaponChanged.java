package com.fouan.events;

import com.fouan.actors.ActorId;
import com.fouan.old.weapons.Weapon;
import lombok.Getter;

@Getter
public final class SurvivorWeaponChanged
        extends Event<SurvivorWeaponChanged>
        implements GameEvent {

    private final ActorId survivorId;
    private final Weapon newWeapon;

    public SurvivorWeaponChanged(int turn, ActorId survivorId, Weapon newWeapon) {
        super(turn);
        this.survivorId = survivorId;
        this.newWeapon = newWeapon;
    }
}

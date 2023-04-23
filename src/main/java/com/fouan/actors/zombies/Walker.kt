package com.fouan.actors.zombies;

import com.fouan.actors.ActorId;

public final class Walker extends Zombie {

    public Walker(ActorId id) {
        super(id, "Walker", 2, 1, 1, 1);
    }
}

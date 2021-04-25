package com.fouan.cards;

import com.fouan.actor.ZombieType;

public class SpawnInfo {
    private final int quantity;
    private final ZombieType type;

    public SpawnInfo(int quantity, ZombieType type) {
        this.quantity = quantity;
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public ZombieType getType() {
        return type;
    }
}

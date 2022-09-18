package com.fouan.old.cards;

import com.fouan.actors.DangerLevel;

import java.util.Map;
import java.util.Optional;

public class ZombieSpawnCard {
    private final String name;
    private final Map<DangerLevel, SpawnInfo> spawnInfoPerDangerLevel;

    public ZombieSpawnCard(String name, Map<DangerLevel, SpawnInfo> spawnInfoPerDangerLevel) {
        this.name = name;
        this.spawnInfoPerDangerLevel = spawnInfoPerDangerLevel;
    }

    public Optional<SpawnInfo> getSpawnInfo(DangerLevel level) {
        return Optional.ofNullable(spawnInfoPerDangerLevel.get(level));
    }
}

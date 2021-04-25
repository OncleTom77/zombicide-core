package com.fouan.board;

import com.fouan.cards.Deck;
import com.fouan.cards.SpawnInfo;
import com.fouan.cards.ZombieSpawnCard;
import com.fouan.actor.Zombie;
import com.fouan.actor.ZombieType;
import com.fouan.io.Output;

import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Named
public class ZombieSpawner {

    private final Random random;
    private final Output output;

    private Deck<ZombieSpawnCard> deck;

    public ZombieSpawner(Random random, Output output) {
        this.random = random;
        this.output = output;
        initDeck();
    }

    private void initDeck() {
        List<ZombieSpawnCard> spawnCards = IntStream.range(0, 10)
                .mapToObj(value -> {
                    Map<DangerLevel, SpawnInfo> spawnInfoPerDangerLevel = Map.of(
                            DangerLevel.BLUE, new SpawnInfo(1, ZombieType.WALKER),
                            DangerLevel.YELLOW, new SpawnInfo(2, ZombieType.WALKER),
                            DangerLevel.ORANGE, new SpawnInfo(3, ZombieType.WALKER),
                            DangerLevel.RED, new SpawnInfo(4, ZombieType.WALKER)
                    );
                    return new ZombieSpawnCard("Simple", spawnInfoPerDangerLevel);
                })
                .collect(Collectors.toList());

        this.deck = new Deck<>(spawnCards);
    }

    public void spawnZombies(DangerLevel level, Zone zone) {
        deck.drawCard()
                .getSpawnInfo(level)
                .ifPresent(spawnInfo -> IntStream.range(0, spawnInfo.getQuantity())
                        .forEach(value -> new Zombie(random, output, spawnInfo.getType(), zone)));
    }
}

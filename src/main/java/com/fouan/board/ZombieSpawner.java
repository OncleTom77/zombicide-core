package com.fouan.board;

import com.fouan.actor.ActorFactory;
import com.fouan.actor.ZombieType;
import com.fouan.cards.Deck;
import com.fouan.cards.SpawnInfo;
import com.fouan.cards.ZombieSpawnCard;
import com.fouan.command.Command;
import com.fouan.command.SpawnZombieCommand;

import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Named
public class ZombieSpawner {

    private final ActorFactory actorFactory;

    private Deck<ZombieSpawnCard> deck;

    public ZombieSpawner(ActorFactory actorFactory) {
        this.actorFactory = actorFactory;
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

    public Optional<Command> spawnZombies(DangerLevel level, Zone zone) {
        return deck.drawCard()
                .getSpawnInfo(level)
                .map(spawnInfo -> new SpawnZombieCommand(spawnInfo, zone, actorFactory));
    }
}

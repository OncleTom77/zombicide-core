package com.fouan.actors.view;

import com.fouan.actors.ActorId;
import com.fouan.actors.Survivor;
import com.fouan.actors.zombies.Zombie;
import com.fouan.zones.Zone;


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ActorsQueries {

    Optional<Survivor> findSurvivorBy(ActorId id);

    Stream<Survivor> allLivingSurvivors();

    Optional<SurvivorWithZone> findSurvivorWithZoneBy(ActorId id);

    Optional<Zombie> findZombieBy(ActorId id);

    Optional<ZombieWithZone> findZombieWithZoneBy(ActorId id);

    List<ZombieWithZone> findZombiesWithZoneNearTo(Zone zone);
}

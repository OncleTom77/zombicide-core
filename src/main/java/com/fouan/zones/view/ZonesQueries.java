package com.fouan.zones.view;

import com.fouan.actors.ActorId;
import com.fouan.zones.Position;
import com.fouan.zones.Zone;
import com.fouan.zones.Zone.ZoneMarker;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ZonesQueries {

    List<Zone> findAll();

    Optional<Zone> findByActorId(ActorId actorId);

    List<Zone> findByMarker(ZoneMarker marker);

    Set<ActorId> findActorIdsOn(Position position);

    List<Zone> findConnectedZones(Zone zone);

    List<Zone> findNoisiestZones();
}

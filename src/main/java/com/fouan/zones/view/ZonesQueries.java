package com.fouan.zones.view;

import com.fouan.actors.ActorId;
import com.fouan.zones.Zone;
import com.fouan.zones.Zone.ZoneMarker;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ZonesQueries {

    Optional<Zone> findByActorId(ActorId actorId);

    List<Zone> findByMarker(ZoneMarker marker);

    Set<ActorId> findActorIdsOn(Zone zone);

    List<Zone> findConnectedZones(Zone zone);
}

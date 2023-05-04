package com.fouan.zones.view

import com.fouan.actors.ActorId
import com.fouan.zones.Position
import com.fouan.zones.Zone
import com.fouan.zones.Zone.ZoneMarker
import java.util.*

interface ZonesQueries {
    fun findAll(): List<Zone>
    fun findAllConnections(): Set<Connection>
    fun findByActorId(actorId: ActorId): Optional<Zone>
    fun findByMarker(marker: ZoneMarker): List<Zone>
    fun findActorIdsOn(position: Position): Set<ActorId>
    fun findConnectedZones(zone: Zone): List<Zone>
    fun findNoisiestZones(withSurvivors: Boolean): List<Zone>
    fun findNoisiestZonesInSight(zone: Zone): List<Zone>
}

package com.fouan.events

import com.fouan.zones.Zone
import com.fouan.zones.view.Connection

class BoardInitialized(turn: Int, zones: List<Zone>, connections: Set<Connection>) :
    Event<BoardInitialized>(turn), ZoneEvent {
    val zones: List<Zone>
    val connections: Set<Connection>

    init {
        this.zones = zones.toMutableList()
        this.connections = connections.toMutableSet()
    }
}

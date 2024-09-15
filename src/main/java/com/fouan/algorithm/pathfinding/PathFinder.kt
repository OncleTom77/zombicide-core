package com.fouan.algorithm.pathfinding

import com.fouan.zones.Zone

interface PathFinder {
    fun findNextZoneOfAllShortestPaths(from: Zone, to: Zone): List<Zone>
}
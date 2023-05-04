package com.fouan.zones.view

import com.fouan.zones.Zone

class Connection(start: Zone, end: Zone) {

    val start: Zone
    val end: Zone

    init {
        require(start != end) { "Start and end must be different" }
        if (Zone.ZONE_POSITION_COMPARATOR.compare(start, end) < 0) {
            this.start = start
            this.end = end
        } else {
            this.start = end
            this.end = start
        }
    }

    fun implies(zone: Zone): Boolean {
        val position = zone.position
        return (start.position == position || end.position == position)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Connection

        if (start != other.start) return false
        return end == other.end
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }

    override fun toString(): String {
        return "Connection(start=$start, end=$end)"
    }

}

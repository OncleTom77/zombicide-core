package com.fouan.zones

class Zone private constructor(
    val position: Position,
    markers: List<ZoneMarker>
) {

    val markers: List<ZoneMarker> = markers
        get() = field.toMutableList()

    enum class ZoneMarker {
        STARTING_ZONE,
        EXIT_ZONE,
        ZOMBIE_SPAWN,
        NORMAL_ZONE
    }

    companion object {
        @JvmField
        val ZONE_POSITION_COMPARATOR: Comparator<Zone> = Comparator.comparing(Zone::position, Position.BOARD_COMPARATOR)

        fun startingZone(position: Position): Zone = Zone(position, listOf(ZoneMarker.STARTING_ZONE))

        fun exitZone(position: Position): Zone = Zone(position, listOf(ZoneMarker.EXIT_ZONE))

        fun zombieSpawnZone(position: Position): Zone = Zone(position, listOf(ZoneMarker.ZOMBIE_SPAWN))

        fun normalZone(position: Position): Zone = Zone(position, listOf(ZoneMarker.NORMAL_ZONE))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Zone

        return position == other.position
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }

    override fun toString(): String {
        return "Zone(position=$position)"
    }
}

package com.fouan.old.board

object ZoneUtils {
    //    public static final Comparator<Zone> compareZonePerNoise = Comparator.comparing(Zone::getNoise);
    @JvmStatic
    fun getNoisiestZones(zones: List<Zone>, withSurvivors: Boolean): List<Zone> {
        val matchingZones = zones.filter { !withSurvivors || it.containsSurvivor() }
        val maxNoise = matchingZones.maxOfOrNull { it.noise }

        return maxNoise?.let {
            matchingZones.filter { zone: Zone -> zone.noise == it }
        } ?: emptyList()
    }
}

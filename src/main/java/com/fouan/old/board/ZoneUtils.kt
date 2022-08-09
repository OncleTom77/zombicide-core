package com.fouan.board

object ZoneUtils {
    //    public static final Comparator<Zone> compareZonePerNoise = Comparator.comparing(Zone::getNoise);
    @JvmStatic
    fun getNoisiestZones(zones: List<Zone>, withSurvivors: Boolean): List<Zone> {
        val matchingZones = zones.filter { zone: Zone -> !withSurvivors || zone.containsSurvivor() }
        val maxNoise: Int? = matchingZones.maxOfOrNull { obj: Zone -> obj.noise }

        return maxNoise?.let { it: Int ->
            matchingZones.filter { zone: Zone -> zone.noise == it }
        } ?: emptyList()
    }
}
package com.fouan.board;

import com.fouan.utils.ListUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ZoneUtils {

    public static final Comparator<Zone> compareZonePerNoise = Comparator.comparing(Zone::getNoise);

    public static List<Zone> getNoisiestZones(List<Zone> zones, boolean withSurvivors) {
        Set<Zone> matchingZones = zones.stream()
                .filter(zone -> !withSurvivors || zone.containsSurvivor())
                .collect(Collectors.toSet());

        return ListUtils.keepMax(matchingZones, compareZonePerNoise);
    }
}

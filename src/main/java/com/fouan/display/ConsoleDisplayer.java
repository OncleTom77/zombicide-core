package com.fouan.display;

import com.fouan.actors.ActorId;
import com.fouan.actors.view.ActorsView;
import com.fouan.events.SurvivorAdded;
import com.fouan.events.SurvivorsTurnStarted;
import com.fouan.events.ZombieSpawned;
import com.fouan.zones.Position;
import com.fouan.zones.Zone;
import com.fouan.zones.view.ZonesView;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;

import javax.inject.Named;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Named
@AllArgsConstructor
public final class ConsoleDisplayer {

    private final Output output;
    private final ZonesView zonesView;
    private final ActorsView actorsView;

    @EventListener
    public void handleSurvivorAdded(SurvivorAdded event) {
        output.display("Survivor " + event.getSurvivor().getName() + " added at " + event.getZone().getPosition().toString());
    }

    @EventListener
    public void handleZombieSpawned(ZombieSpawned event) {
        output.display("Zombie " + event.getZombie().getName() + " spawned at " + event.getZone().getPosition().toString());
    }

    @EventListener
    public void handleSurvivorsTurnStarted(SurvivorsTurnStarted event) {
        // Display board
        Comparator<Zone> zonePositionComparator = (o1, o2) -> Position.BOARD_COMPARATOR.compare(o1.getPosition(), o2.getPosition());
        List<Zone> zones = zonesView.findAll()
                .stream()
                .sorted(zonePositionComparator)
                .toList();
        StringBuilder horizontalLine = new StringBuilder("-");
        int width = getWidth(zones);
        horizontalLine.append("-----".repeat(Math.max(0, width)));

        StringBuilder line = new StringBuilder();
        output.display(horizontalLine.toString());
        for (int i = 0, zonesSize = zones.size(); i < zonesSize; i++) {
            Zone zone = zones.get(i);
            line.append("| ")
                    .append(getStringRepresentation(zone))
                    .append(" ");

            boolean endOfLine = i % width == width - 1;
            if (endOfLine) {
                line.append("|");
                output.display(line.toString());
                line.delete(0, line.length());
                output.display(horizontalLine.toString());
            }
        }
    }

    private int getWidth(List<Zone> zones) {
        return zones.stream()
                .map(Zone::getPosition)
                .mapToInt(Position::getX)
                .max()
                .orElseThrow();
    }

    String getStringRepresentation(Zone zone) {
        Set<ActorId> actorIds = zonesView.findActorIdsOn(zone);
        boolean containsSurvivors = actorIds.stream()
                .anyMatch(id -> actorsView.findSurvivorBy(id).isPresent());
        boolean containsZombies = actorIds.stream()
                .anyMatch(id -> actorsView.findZombieBy(id).isPresent());
        if (containsSurvivors) {
            return "Su";
        } else if (containsZombies) {
            return "Zo";
        } else if (zone.getMarkers().contains(Zone.ZoneMarker.STARTING_ZONE)) {
            return "St";
        } else if (zone.getMarkers().contains(Zone.ZoneMarker.EXIT_ZONE)) {
            return "Ex";
        } else if (zone.getMarkers().contains(Zone.ZoneMarker.ZOMBIE_SPAWN)) {
            return "Sp";
        } else {
            return "  ";
        }
    }
}

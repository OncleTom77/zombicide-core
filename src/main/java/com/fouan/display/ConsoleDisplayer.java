package com.fouan.display;

import com.fouan.actions.Actions;
import com.fouan.actors.ActorId;
import com.fouan.actors.Survivor;
import com.fouan.actors.view.ActorsView;
import com.fouan.events.*;
import com.fouan.game.view.GameView;
import com.fouan.old.io.ChoiceMaker;
import com.fouan.zones.Position;
import com.fouan.zones.Zone;
import com.fouan.zones.view.ZonesView;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;

import javax.inject.Named;
import java.util.*;

@Named
@AllArgsConstructor
public final class ConsoleDisplayer {

    private final Output output;
    private final ZonesView zonesView;
    private final ActorsView actorsView;
    private final GameView gameView;
    private final ChoiceMaker choiceMaker;

    @EventListener
    public void handleSurvivorAdded(SurvivorAdded event) {
        output.display("Survivor " + event.getSurvivor().getName() + " added at " + event.getZone().getPosition().toString());
    }

    @EventListener
    public void handleZombieSpawned(ZombieSpawned event) {
        output.display("Zombie " + event.getZombie().getName() + " spawned at " + event.getZone().getPosition().toString());
    }

    @EventListener
    public void handleAvailableZonesForSurvivorMoveDefined(AvailableZonesForSurvivorMoveDefined event) {
        var availableZones = event.getZones();
        int choice = 0;

        if (availableZones.size() > 1) {
            output.display("Choose your action:");
            for (int i = 0; i < availableZones.size(); i++) {
                output.display(i + ": " + availableZones.get(i));
            }

            choice = choiceMaker.getChoice(0, availableZones.size() - 1);
        }

        var chosenZone = availableZones.get(choice);

        gameView.fireEvent(new ZoneChosen(event.getTurn(), chosenZone.getPosition()));
    }

    @EventListener
    public void handleAvailableActionsDefined(AvailableActionsDefined event) {
        displayBoard();

        var availableActions = event.getActions();
        int choice = 0;

        if (availableActions.size() > 1) {
            output.display("Choose your action:");
            for (int i = 0; i < availableActions.size(); i++) {
                output.display(i + ": " + availableActions.get(i));
            }

            choice = choiceMaker.getChoice(0, availableActions.size() - 1);
        }

        Actions chosenAction = availableActions.get(choice);

        gameView.fireEvent(new ActionChosen(event.getTurn(), chosenAction));
    }

    @EventListener
    public void handleAvailableZombiesForSurvivorAttackDefined(AvailableZombiesForSurvivorAttackDefined event) {
        var availableZombies = event.getZombies().stream().toList();

        output.display("Choose your target:");
        for (int i = 0; i < availableZombies.size(); i++) {
            output.display(i + ": " + availableZombies.get(i));
        }

        Set<Integer> choices = choiceMaker.getChoices(0, availableZombies.size() - 1, event.getNumberOfZombiesToChoose());

        var chosenZombies = choices.stream()
                .map(availableZombies::get)
                .toList();

        gameView.fireEvent(new ZombiesChosen(event.getTurn(), chosenZombies));
    }

    @EventListener
    public void handleSurvivorAttacked(SurvivorAttacked event) {
        Survivor survivor = actorsView.findSurvivorBy(event.getActorId())
                .orElseThrow();
        output.display("Dice roll: " + event.getAttackResult().rolls());
        output.display(survivor.getName() + " attacked with " + event.getAttackResult().weapon() + " and hits " + event.getAttackResult().hitCount() + " target(s)");
    }

    private void displayBoard() {
        // Display board
        List<Zone> zones = zonesView.findAll()
                .stream()
                .sorted(Zone.ZONE_POSITION_COMPARATOR)
                .toList();
        StringBuilder horizontalLine = new StringBuilder("-");
        int width = getWidth(zones);
        horizontalLine.append("-----".repeat(width));

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
                .orElseThrow() + 1;
    }

    private String getStringRepresentation(Zone zone) {
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

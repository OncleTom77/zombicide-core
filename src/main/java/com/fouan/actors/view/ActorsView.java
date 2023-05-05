package com.fouan.actors.view;

import com.fouan.actors.Actor;
import com.fouan.actors.ActorId;
import com.fouan.actors.DangerLevel;
import com.fouan.actors.Survivor;
import com.fouan.actors.zombies.Zombie;
import com.fouan.events.*;
import com.fouan.zones.Zone;
import com.fouan.zones.view.ZonesQueries;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;

import javax.inject.Named;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@Named
@AllArgsConstructor
public final class ActorsView implements ActorsCommands, ActorsQueries {

    private final List<ActorEvent> history = new LinkedList<>();
    private final Actors actors = new Actors();

    private final ZonesQueries zonesQueries;
    private final EventsPublisher eventsPublisher;

    @EventListener
    public void handleActorEvents(ActorEvent event) {
        log.info("{}(turn: {})", event.getClass().getSimpleName(), event.getTurn());
        history.add(event);
    }

    @EventListener
    public void handleSurvivorAdded(SurvivorAdded event) {
        actors.add(event.getSurvivor());
    }

    @EventListener
    public void handleZombieSpawn(ZombieSpawned event) {
        actors.add(event.getZombie());
    }

    @EventListener
    public void handleZombieDied(ZombieDied event) {
        var zombie = findZombieBy(event.getZombieId())
                .orElseThrow(() -> new IllegalStateException("Zombie (id: " + event.getZombieId().getValue() + ") not found"));

        log.info("{} is dead", zombie);
        // TODO: 28/01/2023 Should we add methods in ActorsCommands to add/remove actors?
        actors.remove(zombie);

        eventsPublisher.fire(
                new SurvivorGainedExperience(event.getTurn(), event.getAttackerId(), zombie.getExperienceProvided())
        );
    }

    @EventListener
    public void handleSurvivorLostLifePoints(SurvivorLostLifePoints event) {
        AtomicBoolean survivorDied = new AtomicBoolean(false);
        actors.update(event.getSurvivorId(), actor -> {
            Survivor survivor = (Survivor) actor;
            int remainingLifePoints = survivor.getLifePoints() - event.getDamage();

            if (remainingLifePoints <= 0) {
                survivorDied.set(true);
            }

            return new Survivor(actor.getId(),
                    remainingLifePoints,
                    survivor.getName(),
                    survivor.getWeapon(),
                    survivor.getExperience(),
                    survivor.getActionsCount());
        });

        if (survivorDied.get()) {
            eventsPublisher.fire(new SurvivorDied(event.getTurn(), event.getSurvivorId()));
        }
    }

    @Override
    public void clear() {
        history.clear();
    }

    @Override
    public Actor findActorBy(ActorId id) {
        return actors.findById(id)
                .orElseThrow();
    }

    @Override
    public Optional<Survivor> findLivingSurvivorBy(ActorId id) {
        return actors.findById(id)
                .filter(actor -> actor instanceof Survivor)
                .map(actor -> (Survivor) actor)
                .filter(survivor -> survivor.getLifeStatus() == LifeStatus.ALIVE);
    }

    @Override
    public List<Survivor> allLivingSurvivors() {
        return actors.all()
                .filter(actor -> actor instanceof Survivor)
                .map(actor -> (Survivor) actor)
                .filter(survivor -> survivor.getLifeStatus() == LifeStatus.ALIVE)
                .toList();
    }

    @Override
    public Optional<Zombie> findZombieBy(ActorId id) {
        return actors.findById(id)
                .filter(actor -> actor instanceof Zombie)
                .map(actor -> (Zombie) actor);
    }

    @NotNull
    private List<Zombie> findAllZombies() {
        return actors.all()
                .filter(actor -> actor instanceof Zombie)
                .map(actor -> (Zombie) actor)
                .toList();
    }

    @Override
    public Optional<ZombieWithZone> findZombieWithZoneBy(ActorId id) {
        return findZombieBy(id)
                .map(zombie -> new ZombieWithZone(zombie, zonesQueries.findByActorId(id).orElse(null)));
    }

    @Override
    public Set<Zombie> findAllZombiesOnSameZoneAsSurvivor(ActorId survivorId) {
        Zone survivorZone = zonesQueries.findByActorId(survivorId)
                .orElseThrow();
        return zonesQueries.findActorIdsOn(survivorZone.getPosition())
                .stream()
                .map(this::findZombieBy)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public int getRemainingActionsCountForActor(@NotNull ActorId actorId, int turn) {
        int actionsCount = findActorBy(actorId).getActionsCount();
        if (zombieSpawnedDuringTurn(turn, actorId)) {
            return 0;
        }
        int spentActionsCount = findActionEventForActorIdAndTurn(actorId, turn).size();
        return actionsCount - spentActionsCount;
    }

    private List<ActionEvent> findActionEventForActorIdAndTurn(ActorId actorId, int turn) {
        return history.stream()
                .filter(event -> event instanceof ActionEvent)
                .map(event -> (ActionEvent) event)
                .filter(actionEvent -> actionEvent.getActorId().equals(actorId) && actionEvent.getTurn() == turn)
                .toList();
    }

    @Override
    public Optional<ActorId> findCurrentSurvivorIdForTurn(int turn) {
        return history.stream()
                .filter(actorEvent -> actorEvent instanceof SurvivorsTurnStarted)
                .map(actorEvent -> (SurvivorsTurnStarted) actorEvent)
                .filter(survivorsTurnStarted -> survivorsTurnStarted.getTurn() == turn)
                .map(SurvivorsTurnStarted::getSurvivorId)
                .reduce((first, second) -> second);
    }

    private boolean zombieSpawnedDuringTurn(int turn, ActorId actorId) {
        return history.stream()
                .filter(actorEvent -> actorEvent instanceof ZombieSpawned)
                .map(actorEvent -> (ZombieSpawned) actorEvent)
                .anyMatch(event -> event.getTurn() == turn
                        && event.getZombie().getId().equals(actorId));
    }

    public List<ActorId> findZombieIdsWithRemainingActions(int turn) {
        return findAllZombies().stream()
                .filter(zombie -> !zombieSpawnedDuringTurn(turn, zombie.getId()))
                .filter(zombie -> findActionEventForActorIdAndTurn(zombie.getId(), turn).size() < zombie.getActionsCount())
                .map(Actor::getId)
                .toList();
    }

    @Override
    public List<Zombie> findAttackingZombies() {
        return findAllZombies()
                .stream()
                .filter(zombie -> {
                    Zone zombieZone = zonesQueries.findByActorId(zombie.getId())
                            .orElseThrow();
                    return zonesQueries.findActorIdsOn(zombieZone.getPosition())
                            .stream()
                            .map(this::findLivingSurvivorBy)
                            .anyMatch(Optional::isPresent);
                })
                .toList();
    }

    @Override
    public DangerLevel getCurrentDangerLevel() {
        return allLivingSurvivors()
                .stream()
                .map(Survivor::getDangerLevel)
                .max(DangerLevel.DANGER_LEVEL_COMPARATOR)
                .orElseThrow();
    }
}

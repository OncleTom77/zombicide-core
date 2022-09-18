package com.fouan.game.view;

import com.fouan.actors.view.ActorsCommands;
import com.fouan.dice.DiceRoller;
import com.fouan.events.EndGameEvent;
import com.fouan.events.Event;
import com.fouan.events.EventsPublisher;
import com.fouan.events.GameEvent;
import com.fouan.zones.view.ZonesCommands;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;

import javax.inject.Named;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Named
@AllArgsConstructor
final class DefaultGameView implements GameView {

    private final Deque<GameEvent> gameHistory = new LinkedList<>();
    private final List<Event<?>> history = new LinkedList<>();

    private final ActorsCommands actorsCommands;
    private final ZonesCommands zonesCommands;

    private final EventsPublisher eventsPublisher;
    private final DiceRoller diceRoller;

    @EventListener
    public void handleGameEvents(GameEvent event) {
        gameHistory.push(event);
    }

    @EventListener
    public void handleAllEvents(Event<?> event) {
        history.add(event);
    }

    @Override
    public int getCurrentTurn() {
        return Optional.ofNullable(gameHistory.peekLast())
                .map(GameEvent::getTurn)
                .orElse(0);
    }

    @Override
    public void fireEvent(Event<?> event) {
        eventsPublisher.fire(event);
    }

    @Override
    public int rollDice() {
        return diceRoller.roll();
    }

    @Override
    public boolean isGameDone() {
        return Optional.ofNullable(gameHistory.peekLast())
            .filter(EndGameEvent.class::isInstance)
            .isPresent();
    }

    @Override
    public void rollbackTurn(int turn) {

        var eventsToRestore = history.stream()
                .filter(hist -> hist.getTurn() < turn)
                .toList();

        actorsCommands.clear();
        zonesCommands.clear();
        gameHistory.clear();
        history.clear();

        eventsToRestore.forEach(eventsPublisher::fire);
    }
}

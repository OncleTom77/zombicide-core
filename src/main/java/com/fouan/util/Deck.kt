package com.fouan.old.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Deck<T> {
    protected final LinkedList<T> cards;
    private final List<T> discard;

    public Deck(List<T> cards) {
        this.cards = new LinkedList<>(cards);
        discard = new ArrayList<>();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public T drawCard() {
        if (cards.isEmpty()) {
            refill();
        }
        T card = cards.pop();
        discard.add(card);
        return card;
    }

    public void refill() {
        cards.addAll(discard);
        discard.clear();
        shuffle();
    }

    @Override
    public String toString() {
        return "Deck: " + cards.stream()
                .map(T::toString)
                .collect(Collectors.joining(","));
    }
}

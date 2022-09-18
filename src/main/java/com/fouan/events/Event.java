package com.fouan.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Event<T extends Event> {

    private final int turn;
}

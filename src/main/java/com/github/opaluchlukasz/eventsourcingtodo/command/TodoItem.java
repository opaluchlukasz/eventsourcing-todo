package com.github.opaluchlukasz.eventsourcingtodo.command;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class TodoItem {
    private final String item;
    private final boolean done;
}

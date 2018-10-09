package com.github.opaluchlukasz.eventsourcingtodo.coreapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TodoItemAddedEvent {
    private final String listName;
    private final String item;
}

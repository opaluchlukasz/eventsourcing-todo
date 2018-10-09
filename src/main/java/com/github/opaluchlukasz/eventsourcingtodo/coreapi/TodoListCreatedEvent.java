package com.github.opaluchlukasz.eventsourcingtodo.coreapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TodoListCreatedEvent {
    private final String listName;
}

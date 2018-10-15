package com.github.opaluchlukasz.eventsourcingtodo.coreapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TodoItemDoneEvent {
    private final String listName;
    private final String item;
}

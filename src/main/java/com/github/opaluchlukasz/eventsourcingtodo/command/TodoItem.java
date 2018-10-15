package com.github.opaluchlukasz.eventsourcingtodo.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class TodoItem {
    public final String item;
    public final boolean done;

    public TodoItem done() {
        return new TodoItem(item, true);
    }
}

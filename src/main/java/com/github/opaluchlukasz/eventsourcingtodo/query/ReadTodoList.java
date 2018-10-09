package com.github.opaluchlukasz.eventsourcingtodo.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

//TODO make immutable
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class ReadTodoList implements Serializable {
    private final String listName;
    private final List<Item> todos;

    public ReadTodoList(String listName) {
        this.listName = listName;
        this.todos = new LinkedList<>();
    }

    @RequiredArgsConstructor
    @EqualsAndHashCode(exclude = "modificationDate")
    static class Item implements Serializable {
        private final String item;
        private final boolean done = false;
        private final LocalDateTime modificationDate = LocalDateTime.now();
    }
}

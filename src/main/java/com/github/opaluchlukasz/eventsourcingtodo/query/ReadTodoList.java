package com.github.opaluchlukasz.eventsourcingtodo.query;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

//TODO make immutable
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
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
        final String item;
        final boolean done;
        final LocalDateTime modificationDate = LocalDateTime.now();

        Item done() {
            return new Item(item, true);
        }
    }
}

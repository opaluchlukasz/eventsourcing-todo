package com.github.opaluchlukasz.eventsourcingtodo.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Query {
    public static class FetchListNamesQuery { }

    @RequiredArgsConstructor
    @Getter
    public static class FetchTodoListQuery {
        private final String listName;
    }
}

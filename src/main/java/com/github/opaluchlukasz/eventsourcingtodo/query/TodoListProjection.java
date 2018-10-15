package com.github.opaluchlukasz.eventsourcingtodo.query;

import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoItemAddedEvent;
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoItemDoneEvent;
import com.github.opaluchlukasz.eventsourcingtodo.coreapi.TodoListCreatedEvent;
import com.github.opaluchlukasz.eventsourcingtodo.query.Query.FetchListNamesQuery;
import com.github.opaluchlukasz.eventsourcingtodo.query.Query.FetchTodoListQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
@RequiredArgsConstructor
public class TodoListProjection {
    private final ReadTodoListRepository readTodoListRepository;

    @EventHandler
    public void on(TodoListCreatedEvent event) {
        readTodoListRepository.saveTodoList(new ReadTodoList(event.getListName()));

        List<String> list = readTodoListRepository.findListNames();
        list.add(event.getListName());
        readTodoListRepository.saveListNames(list);
        // TODO handle queryUpdateEmitter
    }

    @EventHandler
    public void on(TodoItemAddedEvent event) {
        ReadTodoList readTodoList = readTodoListRepository.findTodoList(event.getListName());
        readTodoList.getTodos().add(new ReadTodoList.Item(event.getItem(), false));
        readTodoListRepository.saveTodoList(readTodoList);
    }

    @EventHandler
    public void on(TodoItemDoneEvent event) {
        ReadTodoList todoList = readTodoListRepository.findTodoList(event.getListName());
        List<ReadTodoList.Item> todoListItems = todoList.getTodos().stream()
                .map(item -> item.item.equals(event.getItem()) ? item.done() : item)
                .collect(toList());

        readTodoListRepository.saveTodoList(new ReadTodoList(todoList.getListName(), todoListItems));
    }

    @QueryHandler
    public List<String> handle(FetchListNamesQuery fetchListNamesQuery) {
        return readTodoListRepository.findListNames();
    }

    @QueryHandler
    public ReadTodoList handle(FetchTodoListQuery fetchTodoListQuery) {
        return readTodoListRepository.findTodoList(fetchTodoListQuery.getListName());
    }
}